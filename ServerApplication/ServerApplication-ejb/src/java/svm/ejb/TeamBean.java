/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateful;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.modelInterfaces.IContestHasTeam;
import svm.domain.abstraction.modelInterfaces.ITeam;
import svm.domain.abstraction.modeldao.ITeamModelDAO;
import svm.ejb.dto.ContestDTO;
import svm.ejb.dto.TeamDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author mike
 */
@Stateful
public class TeamBean extends ControllerDBSessionBean<ITeamModelDAO> implements TeamBeanRemote {

    private TeamDTO transferTeam;
    private ITeam team;

    public TeamBean() {
        super(DomainFacade.getTeamModelDAO());
    }

    @Override
    public TeamDTO getTeam() {
        return transferTeam;
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
        super.start();
        reattachObjectToSession(team);
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
