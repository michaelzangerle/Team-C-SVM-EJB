/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.List;
import javax.ejb.Remote;
import svm.domain.abstraction.modelInterfaces.IMember;
import svm.ejb.dto.ContestHasTeamDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author mike
 */
@Remote
public interface ContestConfirmationBeanRemote extends SvmBean{
    
     List<ContestHasTeamDTO> getTeamsForNotConfirmedContests() throws  LogicException,PersistenceException;

    /**
     * Confirms or cancels a participation of a team in a contests
     *
     * @return
     */
    void confirmParticipationOfATeam(ContestHasTeamDTO transferTeamHasContest, boolean confirm, String comment, boolean paid) throws LogicException,PersistenceException;

    public void start(IMember member) throws PersistenceException, DomainException, LogicException;
}
