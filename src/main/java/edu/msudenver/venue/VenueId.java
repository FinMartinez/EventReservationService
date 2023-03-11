package edu.msudenver.venue;

import java.io.Serializable;
public class VenueId implements Serializable{

    private Long venueCode;
    private String countryCode;
    private String postalCode;

    public VenueId() {
    }

    public VenueId(Long venueCode, String countryCode, String postalCode) {
        this.venueCode = venueCode;
        this.countryCode = countryCode;
        this.postalCode = postalCode;
    }

    public Long getVenueCode() { return venueCode; }

    public void setVenueCode(Long venueCode) { this.venueCode = venueCode; }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
