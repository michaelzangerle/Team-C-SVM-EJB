/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.jms;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.modelInterfaces.IMember;
import svm.ejb.dto.MemberDTO;
import svm.messages.MemberMessage;
import svm.messages.MessageType;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;

/**
 *
 * @author mike
 */
public class MessageProducer {

    private static Message createJMSMessageForjmsSvmMember(Session session, MemberDTO member) throws JMSException, NoSessionFoundException {

        int sessionId = DomainFacade.generateSessionId();
        IMember messageMember = DomainFacade.getMemberModelDAO().getByUID(sessionId, member.getUID());

        DomainFacade.reattachObjectToSession(sessionId, messageMember.getSport());
        DomainFacade.reattachObjectToSession(sessionId, messageMember.getSport().getDepartment());

        int receiver = messageMember.getSport().getDepartment().getDepartmentHead().getUID();

        DomainFacade.closeSession(sessionId);
        MemberMessage message = new MemberMessage(MessageType.NEW, member.getUID(), receiver, "Member " + member + " added!");

        Message tm = session.createObjectMessage();
        tm.setObjectProperty(session.toString(), message);

        return tm;

    }

    public static void sendJMSMessageToMember(MemberDTO member) throws NamingException, JMSException, NoSessionFoundException {
        Context c = new InitialContext();
        ConnectionFactory cf = (ConnectionFactory) c.lookup("java:comp/env/jms/svm/memberFactory");
        Connection conn = null;
        Session s = null;
        try {
            conn = cf.createConnection();
            s = conn.createSession(false, s.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) c.lookup("java:comp/env/jms/svm/member");
            javax.jms.MessageProducer mp = s.createProducer(destination);
            mp.send(createJMSMessageForjmsSvmMember(s, member));
        } finally {
            if (s != null) {
                s.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
