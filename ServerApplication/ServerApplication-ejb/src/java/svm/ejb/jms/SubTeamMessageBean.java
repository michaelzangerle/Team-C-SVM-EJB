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
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import svm.messages.SubTeamMessage;

/**
 *
 * @author Gigis Home
 */
@MessageDriven(mappedName = "jms/svm/subTeam", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "SubTeamMessageBean"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "SubTeamMessageBean")
})
public class SubTeamMessageBean extends Observable implements MessageListener {
    
    public SubTeamMessageBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        
        try {
            ObjectMessage msg = (ObjectMessage) message;
            Serializable x = msg.getObject();
            receiveSubTeamMessage((SubTeamMessage) x);
        } catch (JMSException ex) {
            Logger.getLogger(MemberMessageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void receiveSubTeamMessage(SubTeamMessage subTeamMessage) {
        if (mySubTeamMessage(subTeamMessage)) {
            informObserver(subTeamMessage);
        }
    }

    private boolean mySubTeamMessage(SubTeamMessage subTeamMessage) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return true;
    }

    private void informObserver(SubTeamMessage subTeamMessage) {
        super.notifyObservers(subTeamMessage);
    }
}
