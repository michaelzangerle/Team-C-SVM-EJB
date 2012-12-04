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
public interface SvmBean {

    void start() throws PersistenceException, DomainException, LogicException;

    void commit() throws LogicException, PersistenceException;

    void abort() throws LogicException, PersistenceException;
}
