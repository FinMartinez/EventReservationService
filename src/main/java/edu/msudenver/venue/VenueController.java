package edu.msudenver.venue;

import edu.msudenver.city.City;
import edu.msudenver.city.CityId;
import edu.msudenver.country.Country;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/venues")
public class VenueController {
    @Autowired
    private VenueService venueService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Venue>> getVenues() {
        try {
            return ResponseEntity.ok(venueService.getVenues());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{countryCode}/{postalCode}", produces = "application/json")
    public ResponseEntity<City> getCity(@PathVariable String countryCode,
                                        @PathVariable String postalCode,
                                        @PathVariable Long venueCode) {
        try {
            VenueId venueId = new VenueId(countryCode, postalCode, venueCode);
            Venue venue = venueService.getVenue(venueId);
            return new ResponseEntity<>(venue, venue == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        try {
            return new ResponseEntity<>(venueService.saveVenue(venue), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/{countryCode}/{postalCode}/{venueCode}",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Venue> updateVenue(@PathVariable String countryCode,
                                           @PathVariable String postalCode,
                                           @PathVariable Long venueCode,
                                           @RequestBody Country updatedCountry) {
        VenueId venueId = new VenueId(venueCode, countryCode, postalCode);
        Venue retrievedVenue = venueService.getVenue(venueId);
        if (retrievedVenue != null) {
            retrievedVenue.setName(updatedCountry.getVenueName());
            try {
                return ResponseEntity.ok(venueService.saveVenue(retrievedVenue));
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{countryCode}/{postalCode}/{venueCode}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long venueCode,
                                           @PathVariable String countryCode,
                                           @PathVariable String postalCode) {
        try {
            VenueId venueId = new VenueId(venueCode, countryCode, postalCode);
            return new ResponseEntity<>(venueService.deleteVenue(venueId) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

}
