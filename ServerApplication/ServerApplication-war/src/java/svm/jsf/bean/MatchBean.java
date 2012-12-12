/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.jsf.bean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import svm.ejb.ContestBeanRemote;
import svm.ejb.dto.MatchDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author Administrator
 */
@Named(value = "matchBean")
@SessionScoped
public class MatchBean implements Serializable {

    private MatchDTO match;
    private Integer home;
    private Integer away;

    /**
     * Creates a new instance of MatchBean
     */
    public MatchBean() {
    }

    public String setMatch(MatchDTO match) {
        this.match = match;
        this.home = match.getResultHome();
        this.away = match.getResultAway();
        return "match";
    }

    public MatchDTO getMatch() {
        return match;
    }

    public Integer getHome() {
        return home;
    }

    public void setHome(Integer home) {
        this.home = home;
    }

    public Integer getAway() {
        return away;
    }

    public void setAway(Integer away) {
        this.away = away;
    }
}
