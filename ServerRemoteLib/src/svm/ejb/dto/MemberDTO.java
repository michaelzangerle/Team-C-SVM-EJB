/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import svm.domain.abstraction.exception.DomainParameterCheckException;
import svm.domain.abstraction.modelInterfaces.IMember;

/**
 *
 * @author Administrator
 */
public class MemberDTO extends DTO<IMember> {

    private String firstName;
    private LocationDTO location;
    private String userName;
    private String lastName;
    private String title;
    private String socialNumber;
    private Date birthDate;
    private String gender;
    private Date entryDate;
    private String phone1;
    private String phone2;
    private String fax;
    private String email1;
    private String email2;
    private String street;
    private String streetNumber;
    private String coordLat;
    private String coordLong;
    private boolean hasPaidFee;
    private SportDTO sport;

    public String getFirstName() {
        return firstName;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public String getUserName() {
        return userName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTitle() {
        return title;
    }

    public String getSocialNumber() {
        return socialNumber;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public String getFax() {
        return fax;
    }

    public String getEmail1() {
        return email1;
    }

    public String getEmail2() {
        return email2;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getCoordLat() {
        return coordLat;
    }

    public String getCoordLong() {
        return coordLong;
    }

    public boolean isHasPaidFee() {
        return hasPaidFee;
    }

    public SportDTO getSport() {
        return sport;
    }

    public MemberDTO(IMember m) {
        super(m);
        doUpdate(m);
    }

    @Override
    protected void doUpdate(IMember member) {
        this.firstName = member.getFirstName();
        this.userName = member.getUserName();
        this.lastName = member.getLastName();
        this.title = member.getTitle();
        this.socialNumber = member.getSocialNumber();
        this.birthDate = member.getBirthDate();
        this.gender = member.getGender();
        this.entryDate = member.getEntryDate();
        Integer integer = new GregorianCalendar().get(Calendar.YEAR);
        try {
            this.hasPaidFee = member.hasPaidFee(integer);
        } catch (DomainParameterCheckException ex) {
            this.hasPaidFee = false;
        }

        if (!member.getContactDetails().isNull()) {
            this.phone1 = member.getContactDetails().getPhone1();
            this.phone2 = member.getContactDetails().getPhone2();
            this.fax = member.getContactDetails().getFax();
            this.email1 = member.getContactDetails().getEmail1();
            this.email2 = member.getContactDetails().getEmail2();
            this.street = member.getContactDetails().getStreet();
            this.streetNumber = member.getContactDetails().getStreetNumber();
            this.coordLat = member.getContactDetails().getCoordLat();
            this.coordLong = member.getContactDetails().getCoordLong();
            if (!member.getContactDetails().getLocation().isNull()) {
                this.location = new LocationDTO(member.getContactDetails().getLocation());
            }
        }

        if (!member.getSport().isNull()) {
            this.sport = new SportDTO(member.getSport());
        }

    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
