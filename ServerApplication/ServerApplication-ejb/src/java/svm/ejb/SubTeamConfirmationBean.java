/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.modelInterfaces.IMember;
import svm.domain.abstraction.modelInterfaces.ISubTeam;
import svm.domain.abstraction.modelInterfaces.ISubTeamsHasMembers;
import svm.domain.abstraction.modeldao.ISubTeamModelDAO;
import svm.ejb.dto.MemberDTO;
import svm.ejb.dto.SubTeamDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;

/**
 *
 * @author mike
 */
@Stateful
public class SubTeamConfirmationBean extends ControllerDBSessionBean<ISubTeamModelDAO> implements SubTeamConfirmationBeanRemote {

    private IMember member;
    private ISubTeam subTeam;
    private MemberDTO memberDTO;
    private SubTeamDTO subTeamDTO;

    public SubTeamConfirmationBean() {
        super(DomainFacade.getSubTeamModelDAO());
    }

    @Override
    public MemberDTO getMember() {
        return this.memberDTO;
    }

    @Override
    public void setConfirmation(boolean confirm, String comment) {
        for (ISubTeamsHasMembers tmp : this.member.getSubTeamsHasMembersForPerson()) {
            if (tmp.getSubTeam().equals(this.subTeam)) {
                tmp.setComment(comment);
                tmp.setConfirmed(confirm);
                return;
            }
        }
    }

    public void start(MemberDTO member, SubTeamDTO subteam) throws PersistenceException, DomainException, LogicException {
        try {
            super.start();
            this.subTeam = DomainFacade.getSubTeamModelDAO().getByUID(getSessionId(), subteam.getUID());
            this.member = DomainFacade.getMemberModelDAO().getByUID(getSessionId(), member.getUID());
            reattachObjectToSession(this.member);
            reattachObjectToSession(this.subTeam);
            this.memberDTO = new MemberDTO(this.member);
            this.subTeamDTO = new SubTeamDTO(this.subTeam);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SubTeamConfirmationBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void restart() throws PersistenceException, DomainException, LogicException {
        start(this.memberDTO, this.subTeamDTO);
    }

    @Override
    public void abort() throws PersistenceException, LogicException {
        super.abort();
    }

    @Override
    public void commit() throws LogicException, PersistenceException {
        startTransaction();
        try {
            DomainFacade.getMemberModelDAO().saveOrUpdate(getSessionId(), member);
            DomainFacade.getSubTeamModelDAO().saveOrUpdate(getSessionId(), subTeam);
        } catch (NoSessionFoundException ex) {
            throw new PersistenceException(ex);
        }

        commitTransaction();

        super.commit();
    }
}
