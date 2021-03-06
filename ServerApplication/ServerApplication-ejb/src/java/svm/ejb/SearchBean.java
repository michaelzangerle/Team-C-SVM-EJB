/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.exception.DomainParameterCheckException;
import svm.domain.abstraction.modelInterfaces.*;
import svm.ejb.dto.*;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;
import svm.persistence.hibernate.HibernateUtil;

/**
 *
 * @author mike
 */
@Stateful
@DeclareRoles({"isAllowedForSearching"})
@RolesAllowed({"isAllowedForSearching"})
public class SearchBean implements SearchBeanRemote {

    private Integer sessionId;

    public SearchBean() {
    }

    public void check() throws PersistenceException {
        if (!HibernateUtil.hasSession(sessionId)) {
            throw new PersistenceException("No Session Found");
        }
    }

    @Override
    @PermitAll
    public void start() throws PersistenceException, DomainException, LogicException {
        this.sessionId = DomainFacade.generateSessionId();
    }

    @Override
    @PermitAll
    public void abort() throws PersistenceException, LogicException {
        try {
            DomainFacade.closeSession(sessionId);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ControllerDBSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.sessionId = null;
    }

    @Override
    @PermitAll
    public void commit() throws LogicException, PersistenceException {
        try {
            DomainFacade.closeSession(sessionId);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ControllerDBSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.sessionId = null;
    }

    @Override
    @PermitAll
    public List<MemberDTO> getMembers(String firstName, String lastName, DepartmentDTO department) throws LogicException, PersistenceException {
        check();
        try {
            List<MemberDTO> result = new LinkedList<MemberDTO>();
            IDepartment d = DomainFacade.getDepartmentModelDAO().getByUID(sessionId, department.getUID());
            for (IMember member : DomainFacade.getMemberModelDAO().get(sessionId, firstName, lastName, d)) {
                result.add(new MemberDTO(member));
            }
            return result;
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    @PermitAll
    public List<MemberDTO> getMembers(String firstName, String lastName, DepartmentDTO department, Boolean paid) throws LogicException, PersistenceException, DomainException {
        check();
        List<MemberDTO> result = new LinkedList<MemberDTO>();
        try {
            if (department != null) {
                IDepartment d = DomainFacade.getDepartmentModelDAO().getByUID(sessionId, department.getUID());
                for (IMember member : DomainFacade.getMemberModelDAO().get(sessionId, firstName, lastName, d)) {
                    if (member.hasPaidFee(new GregorianCalendar().get(Calendar.YEAR)) == paid) {
                        result.add(new MemberDTO(member));
                    }
                }
            } else {
                for (IMember member : DomainFacade.getMemberModelDAO().get(sessionId, firstName, lastName)) {
                    if (member.hasPaidFee(new GregorianCalendar().get(Calendar.YEAR)) == paid) {
                        result.add(new MemberDTO(member));
                    }
                }
            }
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        } catch (DomainParameterCheckException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        }
        return result;
    }

    @Override
    @PermitAll
    public List<MemberDTO> getMembers(String firstName, String lastName) throws LogicException, PersistenceException {
        check();
        List<MemberDTO> result = new LinkedList<MemberDTO>();
        try {
            for (IMember member : DomainFacade.getMemberModelDAO().get(sessionId, firstName, lastName)) {
                result.add(new MemberDTO(member));
            }
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
        return result;
    }

    @Override
    @PermitAll
    public List<MemberDTO> getMembers(Date birthDateFrom, Date birthDateTo) throws PersistenceException, LogicException {
        check();
        List<MemberDTO> result = new LinkedList<MemberDTO>();
        try {
            for (IMember member : DomainFacade.getMemberModelDAO().get(sessionId, birthDateFrom, birthDateTo)) {
                result.add(new MemberDTO(member));
            }
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
        return result;
    }

    @Override
    @PermitAll
    public List<DepartmentDTO> getDepartments() throws LogicException, PersistenceException {
        check();
        List<DepartmentDTO> result = new LinkedList<DepartmentDTO>();
        try {
            for (IDepartment department : DomainFacade.getDepartmentModelDAO().getAll(sessionId)) {
                result.add(new DepartmentDTO(department));
            }
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
        return result;
    }

    @Override
    @PermitAll
    public List<LocationDTO> getLocations() throws LogicException, PersistenceException {
        check();
        List<LocationDTO> result = new LinkedList<LocationDTO>();
        try {
            for (ILocation location : DomainFacade.getLocationModelDAO().get(sessionId, "AT", "Vorarlberg")) {
                result.add(new LocationDTO(location));
            }
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
        return result;
    }

    @Override
    @PermitAll
    public List<ContestDTO> getContests() throws LogicException, PersistenceException {
        check();
        List<ContestDTO> result = new LinkedList<ContestDTO>();
        try {
            for (IContest contest : DomainFacade.getContestModelDAO().getAll(sessionId)) {
                result.add(new ContestDTO(contest));
            }
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
        return result;
    }

    @Override
    @PermitAll
    public List<TeamDTO> getTeams() throws LogicException, PersistenceException {
        check();
        List<TeamDTO> result = new LinkedList<TeamDTO>();
        try {
            for (ITeam team : DomainFacade.getTeamModelDAO().getAll(sessionId)) {
                result.add(new InternalTeamDTO(team));
            }
            for (IExternalTeam extTeam : DomainFacade.getExternalTeamModelDAO().getAll(sessionId)) {
                result.add(new ExternalTeamDTO(extTeam));
            }
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
        return result;
    }

    @Override
    @PermitAll
    public List<UserPrivilegeDTO> getUserPrivileges() throws LogicException, PersistenceException {
        check();
        List<UserPrivilegeDTO> result = new LinkedList<UserPrivilegeDTO>();
        try {
            for (IUserPrivilege priv : DomainFacade.getUserPrivilegeModelDAO().getAll(sessionId)) {
                result.add(new UserPrivilegeDTO(priv));
            }
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }

        return result;
    }

    @Override
    @PermitAll
    public List<SportDTO> getSports() throws LogicException, PersistenceException {
        check();
        List<SportDTO> result = new LinkedList<SportDTO>();
        try {
            for (ISport s : DomainFacade.getSportModelDAO().getAll(sessionId)) {
                result.add(new SportDTO(s));
            }
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
        return result;
    }

    @Override
    @PermitAll
    public MemberDTO getMemberByUID(int uid) throws LogicException, PersistenceException {
        check();
        try {
            return new MemberDTO(DomainFacade.getMemberModelDAO().getByUID(sessionId, uid));
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    @PermitAll
    public SubTeamDTO getSubTeam(int uid) throws LogicException, PersistenceException {
        check();
        try {
            return new SubTeamDTO(DomainFacade.getSubTeamModelDAO().getByUID(sessionId, uid));
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    @PermitAll
    public boolean login(String userName, String password) {
        try {
            List<IMember> members = DomainFacade.getMemberModelDAO().get(sessionId, userName);
            System.out.println("Login: " + userName + " " + members.size());
            if (members.size() == 1) {
                if (password.equals("a")) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
