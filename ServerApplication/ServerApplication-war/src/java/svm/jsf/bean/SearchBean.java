/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.jsf.bean;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import svm.ejb.ContestBeanRemote;
import svm.ejb.SearchBeanRemote;
import svm.ejb.dto.ContestDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author Administrator
 */
@Named(value = "searchBean")
@SessionScoped
public class SearchBean implements Serializable {

    @EJB
    private SearchBeanRemote searchBean;

    public List<ContestDTO> getContests() throws PersistenceException, LogicException, DomainException {
        searchBean.start();
        try {
            return searchBean.getContests();
        } finally {
            searchBean.commit();
        }
    }
}
