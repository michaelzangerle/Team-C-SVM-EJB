/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientapplication;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.ejb.EJB;
import javax.jms.ObjectMessage;
import svm.ejb.MemberBeanRemote;
import svm.ejb.SearchBeanRemote;
import svm.ejb.dto.SportDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.ejb.jms.MemberMessageBeanRemote;
import svm.messages.MemberMessage;

/**
 *
 * @author mike
 */
public class TestMessageBeanMain {

    @EJB
    private static MemberMessageBeanRemote memberMessageBean;
    @EJB
    private static MemberBeanRemote memberController;
    @EJB
    private static SearchBeanRemote searchController;

    public static void main(String[] args) {
    }

    public static void testJMS() throws LogicException, DomainException, PersistenceException {

        searchController.start();
        List<SportDTO> sports = searchController.getSports();
        searchController.abort();

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

        memberController.setSport(sports.get(0));
        memberController.commit();
    }

    public static void testMessageController() {


        memberMessageBean.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                ObjectMessage message = (ObjectMessage) arg;
                try {
                     int m = ((MemberMessage) message.getObject()).getMember();
                     System.out.println("MemberMessage new Member: " + m);
                } catch (Exception e) {
                }
               
            }
        });
    }
}
