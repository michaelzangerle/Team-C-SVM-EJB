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
import javax.ejb.Stateful;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.exception.DomainAttributeException;
import svm.domain.abstraction.exception.DomainParameterCheckException;
import svm.domain.abstraction.modelInterfaces.*;
import svm.domain.abstraction.modeldao.IMemberModelDAO;
import svm.ejb.dto.*;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;
import svm.persistence.abstraction.exceptions.NotSupportedException;

/**
 *
 * @author Administrator
 */
@Stateful
public class MemberBean extends ControllerDBSessionBean<IMemberModelDAO> implements MemberBeanRemote {

    private IMember member;
    private MemberDTO memberDTO;
    private boolean isNewMember;

    public MemberBean() {
        super(DomainFacade.getMemberModelDAO());
    }

    @Override
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
    public void commit() throws LogicException, PersistenceException {
        try {
            startTransaction();
            getModelDAO().saveOrUpdate(getSessionId(), member);
            flush();
            commitTransaction();
            super.commit();
            // TODO JMS
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void abort() throws PersistenceException {
        super.abort();
    }

    @Override
    public MemberDTO getMember() throws LogicException {
        check();
        return memberDTO;
    }

    @Override
    public void setTitle(String title) throws LogicException {
        check();
        member.setTitle(title);
        memberDTO.update(member);
    }

    @Override
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
    public void setPhone1(String val) throws LogicException {
        check();
        member.getContactDetails().setPhone1(val);
        memberDTO.update(member);
    }

    @Override
    public void setPhone2(String val) throws LogicException {
        check();
        member.getContactDetails().setPhone2(val);
        memberDTO.update(member);
    }

    @Override
    public void setEmail1(String val) throws LogicException {
        check();
        member.getContactDetails().setEmail1(val);
        memberDTO.update(member);
    }

    @Override
    public void setEmail2(String val) throws LogicException {
        check();
        member.getContactDetails().setEmail2(val);
        memberDTO.update(member);
    }

    @Override
    public void setFax(String val) throws LogicException {
        check();
        member.getContactDetails().setFax(val);
        memberDTO.update(member);
    }

    @Override
    public void setStreet(String val) throws LogicException {
        check();
        member.getContactDetails().setStreet(val);
        memberDTO.update(member);
    }

    @Override
    public void setStreetNumber(String val) throws LogicException {
        check();
        member.getContactDetails().setStreetNumber(val);
        memberDTO.update(member);
    }

    @Override
    public void setLat(String val) throws LogicException {
        check();
        member.getContactDetails().setCoordLat(val);
        memberDTO.update(member);
    }

    @Override
    public void setLong(String val) throws LogicException {
        check();
        member.getContactDetails().setCoordLong(val);
        memberDTO.update(member);
    }

    @Override
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
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
    public List<UserPrivilegeDTO> getPrivileges() throws LogicException {
        check();
        List<UserPrivilegeDTO> result = new LinkedList<UserPrivilegeDTO>();
        for (IUserPrivilege p : member.getPrivileges()) {
            result.add(new UserPrivilegeDTO(p));
        }
        return result;
    }

    @Override
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
