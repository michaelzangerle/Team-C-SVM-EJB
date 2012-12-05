/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import javax.ejb.Remote;
import svm.ejb.dto.MemberDTO;
import svm.ejb.dto.SubTeamDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author mike
 */
@Remote
public interface SubTeamConfirmationBeanRemote extends SvmBean {
    
    void start(MemberDTO member, SubTeamDTO subteam) throws PersistenceException, DomainException, LogicException;
    
    MemberDTO getMember();

    void setConfirmation(boolean confirm, String comment);
    
}
