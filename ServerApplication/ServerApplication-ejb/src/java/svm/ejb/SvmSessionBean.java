/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.modelInterfaces.IMember;
import svm.ejb.dto.AuthDTO;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;

/**
 *
 * @author Administrator
 */
@Stateful
public class SvmSessionBean implements SvmSessionBeanRemote {

    @Resource
    private SessionContext ejbContext;
    private AuthDTO auth;

    @Override
    public AuthDTO getAuthObject() throws LogicException, PersistenceException {
        if (auth == null) {
            auth = getAuth();
        }
        return auth;
    }

    private AuthDTO getAuth() throws LogicException, PersistenceException {
        Integer sessionId = DomainFacade.generateSessionId();
        try {
            String user = ejbContext.getCallerPrincipal().getName();
            System.out.println(user);
            try {
                List<IMember> m = DomainFacade.getMemberModelDAO().get(sessionId, user);
                System.out.println(m.size());
                if (m.size() != 1) {
                    m = DomainFacade.getMemberModelDAO().get(sessionId, "tf-test");
                }
                System.out.println(m.get(0).getUserName());
                return new AuthDTO(m.get(0));
            } catch (NoSessionFoundException ex) {
                Logger.getLogger(SvmSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                throw new PersistenceException(ex);
            }
        } finally {
            try {
                DomainFacade.closeSession(sessionId);
            } catch (NoSessionFoundException ex) {
                Logger.getLogger(SvmSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                throw new PersistenceException(ex);
            }
        }
    }
}
