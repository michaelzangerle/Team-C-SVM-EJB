/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import svm.domain.abstraction.modelInterfaces.IContactDetails;

/**
 *
 * @author mike
 */
public class ContactDetailsDTO extends DTO<IContactDetails> {

    private String phone1;
    private String phone2;
    private String email1;
    private String email2;
    private String fax;
    private String street;
    private String streetNumber;
    private String coordLat;
    private String coordLong;
    private LocationDTO location;
    
    public ContactDetailsDTO(IContactDetails m) {
        super(m);
        doUpdate(m);
    }
    
    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public String getEmail1() {
        return email1;
    }

    public String getEmail2() {
        return email2;
    }

    public String getFax() {
        return fax;
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

    public LocationDTO getLocation() {
        return location;
    }

    @Override
    protected void doUpdate(IContactDetails contactDetails) { 
        
        this.phone1 = contactDetails.getPhone1();
        this.phone2 = contactDetails.getPhone2();
        this.email1 = contactDetails.getEmail1();
        this.email2 = contactDetails.getEmail2();
        this.fax = contactDetails.getFax();
        this.street = contactDetails.getStreet();
        this.streetNumber = contactDetails.getStreetNumber();
        this.coordLat = contactDetails.getCoordLat();
        this.coordLong = contactDetails.getCoordLong();
        if (!contactDetails.getLocation().isNull()) {
            this.location = new LocationDTO(contactDetails.getLocation());
        }
        
    }
}
