/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import svm.ejb.dto.*;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author mike
 */
@Remote
public interface SearchBeanRemote extends SvmBean {

    List<MemberDTO> getMembers(String firstName, String lastName, DepartmentDTO department) throws LogicException, PersistenceException;

    List<MemberDTO> getMembers(String firstName, String lastName, DepartmentDTO department, Boolean paid) throws LogicException, PersistenceException, DomainException;

    List<MemberDTO> getMembers(String firstName, String lastName) throws LogicException, PersistenceException;

    List<MemberDTO> getMembers(Date birthDateFrom, Date birthDateTo) throws LogicException, PersistenceException;

    List<DepartmentDTO> getDepartments() throws LogicException, PersistenceException;

    List<LocationDTO> getLocations() throws LogicException, PersistenceException;

    List<ContestDTO> getContests() throws LogicException, PersistenceException;

    List<TeamDTO> getTeams() throws LogicException, PersistenceException;

    List<UserPrivilegeDTO> getUserPrivileges() throws LogicException, PersistenceException;

    List<SportDTO> getSports() throws LogicException, PersistenceException;

    MemberDTO getMemberByUID(int uid) throws LogicException, PersistenceException;

    SubTeamDTO getSubTeam(int uid) throws LogicException, PersistenceException;
}
