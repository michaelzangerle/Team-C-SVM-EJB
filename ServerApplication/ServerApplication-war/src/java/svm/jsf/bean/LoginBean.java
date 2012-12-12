/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.jsf.bean;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import svm.ejb.SearchBeanRemote;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author Administrator
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    @EJB
    private SearchBeanRemote searchBean;
    private boolean loggedIn;
    private String userName;
    private String password;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }

    public void login() throws PersistenceException, DomainException, LogicException {
        searchBean.start();
        if (searchBean.login(userName, password)) {
            loggedIn = true;
        } else {
            loggedIn = false;
        }
        searchBean.commit();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
