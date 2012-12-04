/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientapplication;

import java.util.Date;
import java.util.Random;
import javax.ejb.EJB;
import svm.ejb.ContestBeanRemote;
import svm.ejb.MemberBeanRemote;
import svm.ejb.SearchBeanRemote;
import svm.ejb.dto.ContestDTO;
import svm.ejb.dto.MatchDTO;
import svm.ejb.dto.SportDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author mike
 */
public class Main {

    @EJB
    private static MemberBeanRemote memberBean;
    @EJB
    private static SearchBeanRemote searchBean;
    @EJB
    private static ContestBeanRemote contestBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws LogicException, PersistenceException, DomainException {
        // memberBean();

        contest();
    }

    private static void contest() throws PersistenceException, DomainException, LogicException {
        searchBean.start();
        for (ContestDTO contest : searchBean.getContests()) {
            System.out.println(contest.getName());
        }

        ContestDTO contest = searchBean.getContests().get(0);
        searchBean.abort();

        contestBean.start(contest);
        /*
         * ITransferTeam t1 = contestController.getTeams().get(0); ITransferTeam
         * t2 = contestController.getTeams().get(1); ITransferTeam t3 =
         * contestController.getTeams().get(0); ITransferTeam t4 =
         * contestController.getTeams().get(1);
         *
         * contestController.addMatch(t1, t2, new Date(), new Date());
         * contestController.addMatch(t3, t4, new Date(), new Date());
         */
        for (MatchDTO match : contestBean.getMatches()) {
            System.out.println(match.getName());

            contestBean.setDateForMatch(match, new Date());
            contestBean.setResult(match, new Random().nextInt(10), new Random().nextInt(10));
        }
        for (MatchDTO match : contestBean.getMatches()) {
            System.out.println(match.getName());

            contestBean.setDateForMatch(match, new Date());
            contestBean.setResult(match, new Random().nextInt(10), new Random().nextInt(10));
        }

        contestBean.commit();
    }

    private static void memberBean() throws PersistenceException, LogicException, DomainException {
        searchBean.start();
        SportDTO sport = searchBean.getSports().get(0);
        searchBean.commit();

        memberBean.start();
        memberBean.setBirthDate(new Date());
        memberBean.setEntryDate(new Date());
        memberBean.setLat("");
        memberBean.setLong("");
        memberBean.setFirstName("asdf");
        memberBean.setGender("M");
        memberBean.setLastName("asdf");
        memberBean.setSocialNumber("asdf");
        memberBean.setTitle("asdf");
        memberBean.setUsername("sadf");

        memberBean.setSport(sport);
        memberBean.commit();
    }
}
