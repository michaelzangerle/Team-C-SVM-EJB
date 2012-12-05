/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientapplication;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import javax.ejb.EJB;
import svm.ejb.MemberBeanRemote;
import svm.ejb.SearchBeanRemote;

/**
 *
 * @author mike
 */
public class TestMessageBeanMain {
    
    @EJB
    private static MemberBeanRemote memberController;
    
    @EJB
    private static SearchBeanRemote searchController;
    
    
    
    
    public static void main(String[] args) {
        
        
        
    }
    
    
     public static void testJMS() throws svm.persistence.abstraction.exceptions.NotSupportedException, ExistingTransactionException, IllegalGetInstanceException, NoSessionFoundException, NoTransactionException, InstantiationException, IllegalAccessException, RemoteException, DomainParameterCheckException, NotAllowException, DomainAttributeException {

        memberController.start();
        memberController.setBirthDate(new Date());
        memberController.setEntryDate(new Date());
        memberController.setLat("");
        memberController.setLong("");
        memberController.setFirstName("asdf");
        memberController.setGender("M");
        memberController.setLastName("asdf");
        memberController.setSocialNumber("asdf");
        memberController.setTitle("asdf");
        memberController.setUsername("sadf");

        memberController.setSport(sport);
        memberController.commit();
    }

    public static void testMessageController() throws svm.persistence.abstraction.exceptions.NotSupportedException, ExistingTransactionException, IllegalGetInstanceException, NoSessionFoundException, NoTransactionException, InstantiationException, IllegalAccessException, RemoteException, JMSException, InterruptedException {

        
        memberMessageController.addObserver(new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                System.out.println("MemberMessage new Member: " + message.getMember());
            }
        });
        messageController.start();
        // messageController.updateMessages();
    }

  
}
