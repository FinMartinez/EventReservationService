package edu.msudenver.venue;


import com.fasterxml.jackson.annotation.JsonProperty;
import edu.msudenver.city.City;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "venues")
public class Venue {
    @Id
    @Column(name = "venue_id", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long venueId;

    @Column(name = "name", columnDefinition = "varchar(255)")
    @NotNull(message = "name cannot be null")
    private String name;
    @Column(name = "street_address", columnDefinition = "text")
    private String address;

    @Column(name = "type", columnDefinition = "char(7)")
    private String type;

    @Id
    @Column(name = "postal_code", columnDefinition = "varchar(9)")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String postalCode;

    @Id
    @Column(name = "country_code", columnDefinition = "char(2)")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String countryCode;

    @Column(name = "active", columnDefinition = "boolean")
    private boolean active;
    @ManyToOne()
    @JoinColumns({
            @JoinColumn(name = "country_code", referencedColumnName = "country_code", insertable = false, updatable = false),
            @JoinColumn(name = "postal_code", referencedColumnName = "postal_code", insertable = false, updatable = false)
    })
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private City city;

    public Venue() {
    }

    public Venue(Long venueId, String postalCode, String countryCode) {
        this.venueId = venueId;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
    }

    public Long getVenueId() { return venueId; }

    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public String getPostalCode() { return postalCode; }

    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCountryCode() { return countryCode; }

    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
