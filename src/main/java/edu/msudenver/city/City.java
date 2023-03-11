package edu.msudenver.city;


import com.fasterxml.jackson.annotation.JsonProperty;
import edu.msudenver.country.Country;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cities")
public class City {
    @Column(name = "name", columnDefinition = "text")
    @NotNull(message = "name cannot be null")
    private String name;
    @Id
    @Column(name = "postal_code", columnDefinition = "varchar(9)")
    private String postalCode;

    @Id
    @Column(name = "country_code")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String countryCode;

    @ManyToOne()
    @JoinColumn(name = "country_code", referencedColumnName = "country_code", insertable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Country country;

    public City() {
    }

    public City(String name, String postalCode, String countryCode) {
        this.name = name;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
