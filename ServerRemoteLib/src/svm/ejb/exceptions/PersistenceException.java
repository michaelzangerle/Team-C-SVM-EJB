/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.exceptions;

/**
 *
 * @author Administrator
 */
public class PersistenceException extends Exception {

    public PersistenceException() {
        super();
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
