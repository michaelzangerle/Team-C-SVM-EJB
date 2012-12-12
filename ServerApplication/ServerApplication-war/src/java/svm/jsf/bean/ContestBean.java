/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.jsf.bean;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import svm.ejb.ContestBeanRemote;
import svm.ejb.dto.ContestDTO;
import svm.ejb.dto.MatchDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author Administrator
 */
@Named(value = "contestBean")
@SessionScoped
public class ContestBean implements Serializable {

    @EJB
    private ContestBeanRemote contestBean;
    private ContestDTO contest;

    /**
     * Creates a new instance of ContestBean
     */
    public ContestBean() {
    }

    public String setContest(ContestDTO contest) throws PersistenceException, DomainException, LogicException {
        contestBean.start(contest);
        this.contest = contestBean.getTransferContest();
        return "contest";
    }

    public ContestDTO getContest() {
        return this.contest;
    }

    public List<MatchDTO> getMatches() throws PersistenceException, LogicException {
        return contestBean.getMatches();
    }

    public void save(MatchDTO match, Integer home, Integer away) throws DomainException, PersistenceException, LogicException {
        contestBean.setResult(match, home, away);
        contestBean.commit();
        contestBean.restart();
    }
}
