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
 * @author Administrator
 */
@Remote
public interface MemberBeanRemote extends SvmBean {

    MemberDTO getMember() throws javax.ejb.EJBAccessException, LogicException;

    void setTitle(String title) throws javax.ejb.EJBAccessException, LogicException;

    void setFirstName(String firstName) throws javax.ejb.EJBAccessException, LogicException, DomainException;

    void setLastName(String lastName) throws javax.ejb.EJBAccessException, LogicException, DomainException;

    void setSocialNumber(String socialNumber) throws javax.ejb.EJBAccessException, LogicException, DomainException;

    void setBirthDate(Date birthDate) throws javax.ejb.EJBAccessException, LogicException, DomainException;

    void setGender(String gender) throws javax.ejb.EJBAccessException, LogicException, DomainException;

    void setEntryDate(Date entryDate) throws javax.ejb.EJBAccessException, LogicException, DomainException;

    /**
     * Sets the phone1 in the contact details for a member
     *
     * @param val
     */
    void setPhone1(String val) throws javax.ejb.EJBAccessException, LogicException;

    /**
     * Sets the phone2 in the contact details for a member
     *
     * @param val
     */
    void setPhone2(String val) throws javax.ejb.EJBAccessException, LogicException;

    /**
     * Sets the email1 in the contact details for a member
     *
     * @param val
     */
    void setEmail1(String val) throws javax.ejb.EJBAccessException, LogicException;

    /**
     * Sets the email2 in the contact details for a member
     *
     * @param val
     */
    void setEmail2(String val) throws javax.ejb.EJBAccessException, LogicException;

    /**
     * Sets the fax in the contact details for a member
     *
     * @param val
     */
    void setFax(String val) throws javax.ejb.EJBAccessException, LogicException;

    /**
     * Sets the street in the contact details for a member
     *
     * @param val
     */
    void setStreet(String val) throws javax.ejb.EJBAccessException, LogicException;

    /**
     * Sets the street number in the contact details for a member
     *
     * @param val
     */
    void setStreetNumber(String val) throws javax.ejb.EJBAccessException, LogicException;

    /**
     * Sets the latitude in the contact details for a member
     *
     * @param val
     */
    void setLat(String val) throws javax.ejb.EJBAccessException, LogicException;

    /**
     * Sets the longitude in the contact details for a member
     *
     * @param val
     */
    void setLong(String val) throws javax.ejb.EJBAccessException, LogicException;

    /**
     * Sets the location in the contact details for a member
     *
     * @param location
     */
    void setLocation(LocationDTO location) throws javax.ejb.EJBAccessException, LogicException, DomainException, PersistenceException;

    void setPaidCurrentYear() throws javax.ejb.EJBAccessException, LogicException, PersistenceException, DomainException;

    void setUsername(String name) throws javax.ejb.EJBAccessException, LogicException, PersistenceException, DomainException;

    void addPrivilege(UserPrivilegeDTO privilege) throws javax.ejb.EJBAccessException, LogicException, PersistenceException, DomainException;

    void removePrivilege(UserPrivilegeDTO privilege) throws javax.ejb.EJBAccessException, LogicException, PersistenceException, DomainException;

    List<UserPrivilegeDTO> getPrivileges() throws javax.ejb.EJBAccessException, LogicException;

    void setSport(SportDTO sport) throws javax.ejb.EJBAccessException, LogicException, PersistenceException;

    void addMemberToTeam(TeamDTO team) throws javax.ejb.EJBAccessException, PersistenceException, DomainException, LogicException;

    void start(MemberDTO member) throws javax.ejb.EJBAccessException, PersistenceException, DomainException, LogicException;
}
