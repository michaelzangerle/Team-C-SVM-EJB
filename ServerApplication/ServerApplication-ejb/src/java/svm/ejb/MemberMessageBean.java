/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.logic.jms.SvmJMSPublisher;
import svm.messages.MemberMessage;

/**
 *
 * @author Administrator
 */
@Stateful
public class MemberMessageBean implements MemberMessageBeanRemote {

    @EJB
    private SvmSessionBeanRemote svmSessionBean;
    private TopicConnectionFactory tcf;
    private TopicConnection tc1;
    private Topic topicMember;
    private TopicSession ts1;
    private TopicSubscriber subscriber1;

    @Override
    public void start() {
        try {
            //Init Context
            Context context = null;

            context = new InitialContext(SvmJMSPublisher.getContextTable());

            //Hole topic factory
            tcf = (TopicConnectionFactory) context.lookup(SvmJMSPublisher.factoryName);
            //Hole Topic
            topicMember = (Topic) context.lookup(SvmJMSPublisher.memberTopic);
            //Create Topic verbindung
            tc1 = tcf.createTopicConnection();

            //Setzen der Client ID
            String id = svmSessionBean.getAuthObject().getUsername();
            tc1.setClientID(id + "" + SvmJMSPublisher.memberTopic);
            tc1.start();
            //Topic Session starten
            ts1 = tc1.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);

            subscriber1 = ts1.createDurableSubscriber(topicMember, id);
        } catch (LogicException ex) {
            Logger.getLogger(MemberMessageBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PersistenceException ex) {
            Logger.getLogger(MemberMessageBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMSException ex) {
            Logger.getLogger(MemberMessageBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(MemberMessageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<MemberMessage> updateMessages() throws JMSException, LogicException, PersistenceException {
        return checkMessages(subscriber1);
    }

    private List<MemberMessage> checkMessages(TopicSubscriber sub) throws JMSException, LogicException, PersistenceException {
        List<MemberMessage> ls = new LinkedList<MemberMessage>();
        Message m;
        do {
            m = sub.receiveNoWait();
            if (m != null) {
                ObjectMessage msg = (ObjectMessage) m;
                Serializable x = msg.getObject();
                if (x instanceof MemberMessage) {
                    MemberMessage memberMessage = (MemberMessage) x;
                    if (memberMessage.getReceiverUID() == svmSessionBean.getAuthObject().getUID()) {
                        ls.add(memberMessage);
                    }
                }
            }
        } while (m != null);
        return ls;
    }
}
