/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.List;
import javax.ejb.Remote;
import svm.ejb.dto.ContestDTO;
import svm.ejb.dto.MemberDTO;
import svm.ejb.dto.SubTeamDTO;
import svm.ejb.dto.TeamDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author mike
 */
@Remote
public interface SubTeamBeanRemote extends SvmBean{
    
      SubTeamDTO getSubTeam() throws LogicException;

    void setName(String name) throws LogicException;

    void addMember(MemberDTO member) throws LogicException, PersistenceException, DomainException;

    void removeMember(MemberDTO member) throws LogicException, DomainException;

    List<MemberDTO> getMembersOfSubTeam() throws LogicException,PersistenceException;

    List<MemberDTO> getMemberOfTeam() throws LogicException,PersistenceException;
    
    public void start(TeamDTO team, ContestDTO contest) throws PersistenceException, DomainException, LogicException;
    
}
