/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.List;
import javax.ejb.Remote;
import svm.ejb.dto.ContestDTO;
import svm.ejb.dto.TeamDTO;

/**
 *
 * @author mike
 */
@Remote
public interface TeamBeanRemote extends SvmBean {

    TeamDTO getTeam();

    List<ContestDTO> getContests();
}
