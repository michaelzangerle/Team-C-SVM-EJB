/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.messages;

import java.io.Serializable;

/**
 *
 * @author Gigis Home
 */
public interface IMessage extends Serializable {

    int getReceiverUID();
}
