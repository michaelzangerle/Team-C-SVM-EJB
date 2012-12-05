/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.jms;

import java.util.Observer;
import javax.ejb.Remote;

/**
 *
 * @author mike
 */
@Remote
public interface MemberMessageBeanRemote {

    void addObserver(Observer o);

    void deleteObserver(Observer o);
}
