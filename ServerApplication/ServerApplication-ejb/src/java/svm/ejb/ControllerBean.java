/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author Administrator
 */
public abstract class ControllerBean implements SvmBean {

    private Boolean started = false;

    public Boolean isStarted() {
        return started;
    }

    @Override
    public void start() throws PersistenceException, DomainException, LogicException {
        if (isStarted()) {
            abort();
        }
        started = true;
    }

    protected void check() throws LogicException {
        if (!isStarted()) {
            throw new LogicException("Controller not started");
        }
    }
}
