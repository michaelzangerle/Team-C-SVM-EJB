/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.exceptions;

/**
 *
 * @author Administrator
 */
public class DomainException extends Exception {

    public DomainException() {
        super();
    }

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainException(Throwable cause) {
        super(cause);
    }
}
