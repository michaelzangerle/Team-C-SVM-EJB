/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.modelInterfaces.IContestHasTeam;
import svm.domain.abstraction.modelInterfaces.IMember;
import svm.domain.abstraction.modeldao.IContestModelDAO;
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
public class ContestConfirmationBean extends ControllerDBSessionBean<IContestsHasTeamsModelDao> implements ContestConfirmationBeanRemote {

    List<ContestHasTeamDTO> contestHasTeamDTOs;
    private IMember member;
    MemberDTO memberDTO;

    public ContestConfirmationBean() {
        super(DomainFacade.getContestsHasTeamsDAO());
    }

    @Override
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
    public void abort() throws svm.ejb.exceptions.PersistenceException, LogicException {
        super.abort();
    }

    @Override
    public List<ContestHasTeamDTO> getTeamsForNotConfirmedContests() throws LogicException, PersistenceException {
        check();
        List<ContestHasTeamDTO> result = new LinkedList<ContestHasTeamDTO>();

        for (IContestHasTeam tmp : member.getContestsHasTeamsForPerson()) {
            result.add(new ContestHasTeamDTO(tmp));
        }

        return result;
    }

    @Override
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
