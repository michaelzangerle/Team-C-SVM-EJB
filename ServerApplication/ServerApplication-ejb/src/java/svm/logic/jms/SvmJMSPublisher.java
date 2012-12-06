package svm.logic.jms;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.modelInterfaces.IMember;
import svm.domain.abstraction.modelInterfaces.ISubTeam;
import svm.ejb.dto.MemberDTO;
import svm.ejb.dto.SubTeamDTO;
import svm.ejb.exceptions.PersistenceException;
import svm.messages.MemberMessage;
import svm.messages.MessageType;
import svm.messages.SubTeamMessage;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;

/**
 * ProjectTeam: Team C Date: 21.11.12
 */
public class SvmJMSPublisher {

    private class MyTopicSession {

        private Topic topic;
        private TopicSession topicSession;
        private TopicPublisher topicPublisher;

        public MyTopicSession(String topicName) throws NamingException, JMSException {
            topic = (Topic) ctx.lookup(topicName);
            topicSession = topicConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topicPublisher = topicSession.createPublisher(topic);
            topicPublisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        }

        public void sendMessage(Serializable obj) throws JMSException {
            ObjectMessage message = topicSession.createObjectMessage();
            message.setObject(obj);
            topicPublisher.publish(message);
        }
    }
    private static SvmJMSPublisher instance;
    public static final String subTeamTopic = "svm/subTeam";
    public static final String memberTopic = "svm/member";
    public static final String factoryName = "java:tf";
    public static final String provider = "file:///C:/temp";
    public static final String initialFactory = "com.sun.jndi.fscontext.RefFSContextFactory";

    public static SvmJMSPublisher getInstance() throws NamingException, JMSException {
        if (instance == null) {
            instance = new SvmJMSPublisher();
        }
        return instance;
    }

    public static Hashtable<String, String> getContextTable() {
        Hashtable<String, String> map = new Hashtable<String, String>();
        map.put("java.naming.provider.url", provider);
        map.put("java.naming.factory.initial", initialFactory);
        return map;
    }
    private InitialContext ctx;
    private TopicConnectionFactory connFactory;
    private TopicConnection topicConn;
    private MyTopicSession subTeamTopicSession;
    private MyTopicSession memberTopicSession;

    private SvmJMSPublisher() throws NamingException, JMSException {
        init();
    }

    private void init() throws NamingException, JMSException {
        // Create InitialContext
        ctx = new InitialContext(getContextTable());
        // lookup the topic connection factory
        connFactory = (TopicConnectionFactory) ctx.lookup(factoryName);
        // create a topic connection
        topicConn = connFactory.createTopicConnection();
        topicConn.start();

        // GET Topics
        subTeamTopicSession = new MyTopicSession(subTeamTopic);
        memberTopicSession = new MyTopicSession(memberTopic);
    }

    public void sendNewMember(IMember member) throws JMSException, PersistenceException {
        sendNewMember(new MemberDTO(member));
    }

    public void sendNewMember(MemberDTO member) throws JMSException, PersistenceException {
        int sessionId = DomainFacade.generateSessionId();
        try {
            IMember messageMember = DomainFacade.getMemberModelDAO().getByUID(sessionId, member.getUID());
            DomainFacade.reattachObjectToSession(sessionId, messageMember.getSport());
            DomainFacade.reattachObjectToSession(sessionId, messageMember.getSport().getDepartment());
            int receiver = messageMember.getSport().getDepartment().getDepartmentHead().getUID();
            memberTopicSession.sendMessage(new MemberMessage(MessageType.NEW, member.getUID(), receiver, "Member " + member + " added!"));
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SvmJMSPublisher.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        } finally {
            try {
                DomainFacade.closeSession(sessionId);
            } catch (NoSessionFoundException ex) {
                Logger.getLogger(SvmJMSPublisher.class.getName()).log(Level.SEVERE, null, ex);
                throw new PersistenceException(ex);
            }
        }
    }

    public void sendMemberAddToSubTeam(IMember member, ISubTeam subTeam, String text) throws JMSException {
        sendMemberAddToSubTeam(new MemberDTO(member), new SubTeamDTO(subTeam), text);
    }

    public void sendMemberAddToSubTeam(MemberDTO member, SubTeamDTO subTeam, String text) throws JMSException {
        subTeamTopicSession.sendMessage(new SubTeamMessage(MessageType.ADDED, member.getUID(), subTeam.getUID(), text));
    }

    public void sendMemberRemoveFormSubTeam(IMember member, ISubTeam subTeam, String text) throws JMSException {
        sendMemberRemoveFormSubTeam(new MemberDTO(member), new SubTeamDTO(subTeam), text);
    }

    public void sendMemberRemoveFormSubTeam(MemberDTO member, SubTeamDTO subTeam, String text) throws JMSException {
        subTeamTopicSession.sendMessage(new SubTeamMessage(MessageType.REMOVED, member.getUID(), subTeam.getUID(), text));
    }
}
