/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateful;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.modelInterfaces.IContestHasTeam;
import svm.domain.abstraction.modelInterfaces.IExternalTeam;
import svm.domain.abstraction.modelInterfaces.ITeam;
import svm.domain.abstraction.modeldao.ITeamModelDAO;
import svm.ejb.dto.ContestDTO;
import svm.ejb.dto.InternalTeamDTO;
import svm.ejb.dto.TeamDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;
import svm.persistence.abstraction.exceptions.NotSupportedException;

/**
 *
 * @author mike
 */
@Stateful
public class TeamBean extends ControllerDBSessionBean<ITeamModelDAO> implements TeamBeanRemote {

    private TeamDTO teamDTO;
    private ITeam team;

    public TeamBean() {
        super(DomainFacade.getTeamModelDAO());
    }

    @Override
    public TeamDTO getTeam() {
        return teamDTO;
    }

    @Override
    public List<ContestDTO> getContests() {
        List<ContestDTO> contests = new LinkedList<ContestDTO>();
        for (IContestHasTeam contestHasTeam : this.team.getAllContests()) {
            contests.add(new ContestDTO(contestHasTeam.getContest()));
        }
        return contests;
    }

    @Override
    public void start() throws PersistenceException, DomainException, LogicException {
        try {
            super.start();
            team = getModelDAO().generateObject(getSessionId());
            reattachObjectToSession(team);
            teamDTO = new InternalTeamDTO(team);
        } catch (InstantiationException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        }
    }

    @Override
    @PermitAll
    public void start(TeamDTO team) throws PersistenceException, DomainException, LogicException {
        super.start();
        if (team instanceof IExternalTeam) {
            throw new LogicException("No External Team supported");
        }
        try {
            this.team = getModelDAO().getByUID(getSessionId(), team.getUID());
            reattachObjectToSession(this.team);
            teamDTO = new InternalTeamDTO(this.team);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ContestBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void restart() throws PersistenceException, DomainException, LogicException {
        start(this.teamDTO);
    }

    @Override
    public void abort() throws PersistenceException, LogicException {
        super.abort();

    }

    @Override
    public void commit() throws LogicException, PersistenceException {
        super.commit();
    }
}
