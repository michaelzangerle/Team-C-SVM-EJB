/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.List;
import java.util.Observer;
import javax.ejb.Remote;
import javax.jms.JMSException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.messages.SubTeamMessage;

/**
 *
 * @author Administrator
 */
@Remote
public interface SubTeamMessageBeanRemote {

    void start();

    List<SubTeamMessage> updateMessages() throws JMSException, LogicException, PersistenceException;
}
