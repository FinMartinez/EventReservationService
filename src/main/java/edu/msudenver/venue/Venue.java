package edu.msudenver.venue;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "venues")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "venue_id", columnDefinition = "SERIAL")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long venueId;

    @Column(name = "name", columnDefinition = "varchar(255)")
    @NotNull(message = "name cannot be null")
    private String venueName;
    @Column(name = "street_address", columnDefinition = "text")
    private String venueAddress;

    @Column(name = "type", columnDefinition = "char(7)")
    private String venueType;

    @Column(name = "postal_code", columnDefinition = "varchar(9)")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String postalCode;

    @Column(name = "country_code", columnDefinition = "char(2)")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String countryCode;

    @Column(name = "active", columnDefinition = "boolean")
    private boolean active;

    public Venue() {
    }

    public Venue(Long venueId,
                 String venueName,
                 String venueAddress,
                 String venueType,
                 String postalCode,
                 String countryCode,
                 Boolean active) {
        this.venueId = venueId;
        this.venueName = venueName;
        this.venueAddress = venueAddress;
        this.venueType = venueType;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
        this.active = active;
    }

    public Long getVenueId() { return venueId; }

    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public String getVenueName() { return venueName;}

    public void setVenueName(String venueName) { this.venueName = venueName; }

    public String getVenueAddress() { return venueAddress; }

    public void setVenueAddress(String venueAddress) { this.venueAddress = venueAddress; }

    public String getVenueType() { return venueType; }

    public void setVenueType(String venueType) { this.venueType = venueType; }
    public String getPostalCode() { return postalCode; }

    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCountryCode() { return countryCode; }

    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }
}
