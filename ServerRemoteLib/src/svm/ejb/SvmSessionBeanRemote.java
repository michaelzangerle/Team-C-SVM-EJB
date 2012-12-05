/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import javax.ejb.Remote;
import svm.ejb.dto.AuthDTO;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author Administrator
 */
@Remote
public interface SvmSessionBeanRemote {

    AuthDTO getAuthObject() throws LogicException, PersistenceException;
    
}
