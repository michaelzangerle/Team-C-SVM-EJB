/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.jms;

import java.io.Serializable;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.Stateful;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import svm.messages.MemberMessage;

/**
 *
 * @author Gigis Home
 */
@MessageDriven(mappedName = "jms/svm/member", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "MessageBean"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "MessageBean")
})

@Stateful
public class MemberMessageBean extends Observable implements MessageListener, MemberMessageBeanRemote {
    
    public MemberMessageBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage msg = (ObjectMessage) message;
            Serializable x = msg.getObject();
            receiveMemberMessage((MemberMessage) x);
        } catch (JMSException ex) {
            Logger.getLogger(MemberMessageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    private void receiveMemberMessage(MemberMessage x) {
        if (myMemberMessage(x)) {
            informObserver(x);
        }
    }
    
    private void informObserver(MemberMessage message) {
        super.notifyObservers(message);
    }
    
    private boolean myMemberMessage(MemberMessage x) {
        return true;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

}
