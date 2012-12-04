/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import svm.domain.abstraction.modelInterfaces.ILocation;

/**
 *
 * @author Administrator
 */
public class LocationDTO extends DTO<ILocation> {

    private String countryCode;
    private String postalCode;
    private String placeName;
    private String district;
    private String province;
    private String community;
    private String latitude;
    private String longitude;

    public String getCountryCode() {
        return countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getDistrict() {
        return district;
    }

    public String getProvince() {
        return province;
    }

    public String getCommunity() {
        return community;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    
    public LocationDTO(ILocation m) {
        super(m);
        doUpdate(m);
    }

    @Override
    protected void doUpdate(ILocation location) {
        this.countryCode = location.getCountryCode();
        this.postalCode = location.getPostalCode();
        this.placeName = location.getPlaceName();
        this.district = location.getDistrict();
        this.province = location.getProvince();
        this.community = location.getCommunity();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}
