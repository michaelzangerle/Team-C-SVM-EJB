/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.modelInterfaces.IContestHasTeam;
import svm.domain.abstraction.modelInterfaces.IMember;
import svm.domain.abstraction.modeldao.IContestsHasTeamsModelDao;
import svm.ejb.dto.ContestHasTeamDTO;
import svm.ejb.dto.MemberDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;

/**
 *
 * @author mike
 */
@Stateful
@DeclareRoles({"isAllowedForAll"})
@RolesAllowed({"isAllowedForAll"})
public class ContestConfirmationBean extends ControllerDBSessionBean<IContestsHasTeamsModelDao> implements ContestConfirmationBeanRemote {

    List<ContestHasTeamDTO> contestHasTeamDTOs;
    private IMember member;
    MemberDTO memberDTO;

    public ContestConfirmationBean() {
        super(DomainFacade.getContestsHasTeamsDAO());
    }

    @Override
    @PermitAll
    public void start(IMember member) throws PersistenceException, DomainException, LogicException {
        super.start();
        this.member = member;
        reattachObjectToSession(member);
        memberDTO = new MemberDTO(member);

        contestHasTeamDTOs = new LinkedList<ContestHasTeamDTO>();

        for (IContestHasTeam tmp : member.getContestsHasTeamsForPerson()) {
            this.contestHasTeamDTOs.add(new ContestHasTeamDTO(tmp));
        }
    }

    @Override
    @PermitAll
    public void restart() throws PersistenceException, DomainException, LogicException {
        throw new UnsupportedOperationException();
    }

    @Override
    @PermitAll
    public void commit() throws LogicException, svm.ejb.exceptions.PersistenceException {
        try {
            startTransaction();
            for (ContestHasTeamDTO cht : contestHasTeamDTOs) {
                IContestHasTeam cht2 = DomainFacade.getContestsHasTeamsDAO().getByUID(getSessionId(), cht.getUID());
                getModelDAO().saveOrUpdate(getSessionId(), cht2);
            }
            flush();
            commitTransaction();

        } catch (NoSessionFoundException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new svm.ejb.exceptions.PersistenceException(ex);
        }
    }

    @Override
    @PermitAll
    public void abort() throws svm.ejb.exceptions.PersistenceException, LogicException {
        super.abort();
    }

    @Override
    @PermitAll
    public List<ContestHasTeamDTO> getTeamsForNotConfirmedContests() throws LogicException, PersistenceException {
        check();
        List<ContestHasTeamDTO> result = new LinkedList<ContestHasTeamDTO>();

        for (IContestHasTeam tmp : member.getContestsHasTeamsForPerson()) {
            result.add(new ContestHasTeamDTO(tmp));
        }

        return result;
    }

    @Override
    @PermitAll
    public void confirmParticipationOfATeam(ContestHasTeamDTO transferTeamHasContest, boolean confirm, String comment, boolean paid) throws LogicException, PersistenceException {
        check();
        try {
            IContestHasTeam contestHasTeam = DomainFacade.getContestsHasTeamsDAO().getByUID(getSessionId(), transferTeamHasContest.getUID());
            contestHasTeam.setComment(comment);
            contestHasTeam.setConfirmed(confirm);
            contestHasTeam.setPaid(paid);
        } catch (NoSessionFoundException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }

        for (ContestHasTeamDTO contestHasTeamDTO : contestHasTeamDTOs) {
            try {
                contestHasTeamDTO.update(DomainFacade.getContestsHasTeamsDAO().getByUID(getSessionId(), contestHasTeamDTO.getUID()));
            } catch (NoSessionFoundException ex) {
                throw new PersistenceException(ex.getMessage(), ex);
            }
        }
    }
}
