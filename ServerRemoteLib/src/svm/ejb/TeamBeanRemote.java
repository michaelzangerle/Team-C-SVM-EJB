/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.List;
import javax.ejb.Remote;
import svm.ejb.dto.ContestDTO;
import svm.ejb.dto.TeamDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author mike
 */
@Remote
public interface TeamBeanRemote extends SvmBean {

    TeamDTO getTeam();

    List<ContestDTO> getContests();

    void start(TeamDTO team) throws PersistenceException, DomainException, LogicException;
}
