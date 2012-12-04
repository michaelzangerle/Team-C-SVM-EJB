/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import javax.ejb.Remote;
import svm.ejb.dto.MemberDTO;

/**
 *
 * @author mike
 */
@Remote
public interface SubTeamConfirmationBeanRemote extends SvmBean {
    
    MemberDTO getMember();

    void setConfirmation(boolean confirm, String comment);
    
}
