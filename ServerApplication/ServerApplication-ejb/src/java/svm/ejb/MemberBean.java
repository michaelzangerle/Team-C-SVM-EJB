/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.jms.JMSException;
import javax.naming.NamingException;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.exception.DomainAttributeException;
import svm.domain.abstraction.exception.DomainParameterCheckException;
import svm.domain.abstraction.modelInterfaces.*;
import svm.domain.abstraction.modeldao.IMemberModelDAO;
import svm.ejb.dto.*;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.logic.jms.SvmJMSPublisher;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;
import svm.persistence.abstraction.exceptions.NotSupportedException;

/**
 *
 * @author Administrator
 */
@Stateful
@DeclareRoles({"isAllowedForMemberChanging", "isAllowedForMemberAddingPrivileges"})
@RolesAllowed({"isAllowedForMemberChanging", "isAllowedForMemberAddingPrivileges"})
public class MemberBean extends ControllerDBSessionBean<IMemberModelDAO> implements MemberBeanRemote {

    private IMember member;
    private MemberDTO memberDTO;
    private boolean isNewMember;

    public MemberBean() {
        super(DomainFacade.getMemberModelDAO());
    }

    @Override
    @PermitAll
    public void start(MemberDTO member) throws PersistenceException, DomainException, LogicException {
        try {
            super.start();
            this.member = DomainFacade.getMemberModelDAO().getByUID(getSessionId(), member.getUID());
            isNewMember = false;
            reattachObjectToSession(this.member);
            memberDTO = new MemberDTO(this.member);
        } catch (NoSessionFoundException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    @PermitAll
    public void restart() throws PersistenceException, DomainException, LogicException {
        start(this.memberDTO);
    }

    @Override
    @PermitAll
    public void start() throws PersistenceException, DomainException, LogicException {
        try {
            super.start();
            member = getModelDAO().generateObject(getSessionId());
            isNewMember = true;
            reattachObjectToSession(member);
            memberDTO = new MemberDTO(member);
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
    public void commit() throws LogicException, PersistenceException {
        try {
            startTransaction();
            getModelDAO().saveOrUpdate(getSessionId(), member);
            flush();
            commitTransaction();
            super.commit();
            if (isNewMember) {
                try {
                    SvmJMSPublisher.getInstance().sendNewMember(member);
                } catch (JMSException ex) {
                    Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NamingException ex) {
                    Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                isNewMember = false;
            }
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    @PermitAll
    public void abort() throws PersistenceException, LogicException {
        super.abort();
    }

    @Override
    @PermitAll
    public MemberDTO getMember() throws LogicException {
        check();
        return memberDTO;
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setTitle(String title) throws LogicException {
        check();
        member.setTitle(title);
        memberDTO.update(member);
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setFirstName(String firstName) throws LogicException, DomainException {
        check();
        try {
            member.setFirstName(firstName);
            memberDTO.update(member);
        } catch (DomainAttributeException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        }
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setLastName(String lastName) throws LogicException, DomainException {
        check();
        try {
            member.setLastName(lastName);
            memberDTO.update(member);
        } catch (DomainAttributeException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        }
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setSocialNumber(String socialNumber) throws LogicException, DomainException {
        check();
        try {
            member.setSocialNumber(socialNumber);
            memberDTO.update(member);
        } catch (DomainAttributeException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        }
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setBirthDate(Date birthDate) throws DomainException, LogicException {
        check();
        try {
            member.setBirthDate(birthDate);
            memberDTO.update(member);
        } catch (DomainParameterCheckException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        }
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setGender(String gender) throws DomainException, LogicException {
        check();
        try {
            member.setGender(gender);
            memberDTO.update(member);
        } catch (DomainAttributeException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (DomainParameterCheckException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        }
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setEntryDate(Date entryDate) throws DomainException, LogicException {
        check();
        try {
            member.setEntryDate(entryDate);
            memberDTO.update(member);
        } catch (DomainParameterCheckException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        }
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setPhone1(String val) throws LogicException {
        check();
        member.getContactDetails().setPhone1(val);
        memberDTO.update(member);
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setPhone2(String val) throws LogicException {
        check();
        member.getContactDetails().setPhone2(val);
        memberDTO.update(member);
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setEmail1(String val) throws LogicException {
        check();
        member.getContactDetails().setEmail1(val);
        memberDTO.update(member);
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setEmail2(String val) throws LogicException {
        check();
        member.getContactDetails().setEmail2(val);
        memberDTO.update(member);
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setFax(String val) throws LogicException {
        check();
        member.getContactDetails().setFax(val);
        memberDTO.update(member);
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setStreet(String val) throws LogicException {
        check();
        member.getContactDetails().setStreet(val);
        memberDTO.update(member);
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setStreetNumber(String val) throws LogicException {
        check();
        member.getContactDetails().setStreetNumber(val);
        memberDTO.update(member);
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setLat(String val) throws LogicException {
        check();
        member.getContactDetails().setCoordLat(val);
        memberDTO.update(member);
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setLong(String val) throws LogicException {
        check();
        member.getContactDetails().setCoordLong(val);
        memberDTO.update(member);
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setLocation(LocationDTO location) throws LogicException, DomainException, PersistenceException {
        check();
        try {
            ILocation loc = DomainFacade.getLocationModelDAO().getByUID(getSessionId(), location.getUID());
            member.getContactDetails().setLocation(loc);
            memberDTO.update(member);
        } catch (DomainAttributeException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setPaidCurrentYear() throws LogicException, PersistenceException, DomainException {
        check();
        try {
            member.setPaidCurrentYear();
            memberDTO.update(member);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        }
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setUsername(String name) throws LogicException, DomainException {
        check();
        try {
            this.member.setUserName(name);
            memberDTO.update(member);
        } catch (DomainAttributeException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        }
        memberDTO.update(member);
    }

    @Override
    @RolesAllowed("isAllowedForMemberAddingPrivileges")
    public void addPrivilege(UserPrivilegeDTO privilege) throws LogicException, PersistenceException, DomainException {
        check();
        try {
            IUserPrivilege up = DomainFacade.getUserPrivilegeModelDAO().getByUID(getSessionId(), privilege.getUID());
            member.addPrivilege(up);
            memberDTO.update(member);
        } catch (DomainParameterCheckException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (DomainAttributeException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    @RolesAllowed("isAllowedForMemberAddingPrivileges")
    public void removePrivilege(UserPrivilegeDTO privilege) throws LogicException, DomainException, PersistenceException {
        check();
        try {
            IUserPrivilege up = DomainFacade.getUserPrivilegeModelDAO().getByUID(getSessionId(), privilege.getUID());
            member.removePrivilege(up);
            memberDTO.update(member);
        } catch (DomainParameterCheckException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (DomainAttributeException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    @PermitAll
    public List<UserPrivilegeDTO> getPrivileges() throws LogicException {
        check();
        List<UserPrivilegeDTO> result = new LinkedList<UserPrivilegeDTO>();
        for (IUserPrivilege p : member.getPrivileges()) {
            result.add(new UserPrivilegeDTO(p));
        }
        return result;
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void setSport(SportDTO sport) throws LogicException, PersistenceException {
        check();
        try {
            ISport sp = DomainFacade.getSportModelDAO().getByUID(getSessionId(), sport.getUID());
            member.setSport(sp);
            memberDTO.update(member);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    @RolesAllowed("isAllowedForMemberChanging")
    public void addMemberToTeam(TeamDTO team) throws LogicException, DomainException, PersistenceException {
        check();
        try {
            ITeam t = DomainFacade.getTeamModelDAO().getByUID(getSessionId(), team.getUID());
            t.addMemberToTeam(member);
            memberDTO.update(member);
        } catch (NotSupportedException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }
}
