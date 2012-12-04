/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.logging.Level;
import java.util.logging.Logger;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.modelInterfaces.IModel;
import svm.domain.abstraction.modeldao.IModelDAO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.persistence.abstraction.exceptions.ExistingTransactionException;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;
import svm.persistence.abstraction.exceptions.NoTransactionException;
import svm.persistence.hibernate.HibernateUtil;

/**
 *
 * @author Administrator
 */
public abstract class ControllerDBSessionBean<T extends IModelDAO> extends ControllerBean {

    private Integer sessionId;
    private T modelDAO;

    public ControllerDBSessionBean(T modelDAO) {
        this.modelDAO = modelDAO;
    }

    protected Integer getSessionId() {
        return sessionId;
    }

    protected T getModelDAO() {
        return modelDAO;
    }

    protected void reattachObjectToSession(IModel model) throws LogicException, PersistenceException {
        check();
        try {
            DomainFacade.reattachObjectToSession(getSessionId(), model);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ControllerDBSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    protected void startTransaction() throws LogicException, PersistenceException {
        try {
            check();
            DomainFacade.startTransaction(getSessionId());
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ControllerDBSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        } catch (ExistingTransactionException ex) {
            Logger.getLogger(ControllerDBSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    protected void commitTransaction() throws LogicException, PersistenceException {
        try {
            check();
            DomainFacade.commitTransaction(getSessionId());
        } catch (NoTransactionException ex) {
            Logger.getLogger(ControllerDBSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ControllerDBSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    protected void flush() throws LogicException, PersistenceException {
        try {
            check();
            HibernateUtil.getSession(sessionId).flush();
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ControllerDBSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void start() throws PersistenceException, DomainException, LogicException {
        super.start();
        this.sessionId = DomainFacade.generateSessionId();
    }

    @Override
    public void abort() throws PersistenceException, LogicException {
        super.abort();
        try {
            DomainFacade.closeSession(sessionId);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ControllerDBSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
        this.sessionId = null;
    }

    @Override
    public void commit() throws LogicException, PersistenceException {
        super.commit();
        try {
            DomainFacade.closeSession(sessionId);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ControllerDBSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
        this.sessionId = null;
    }
}
