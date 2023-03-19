package edu.msudenver.venue;

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

    @GetMapping(path = "/{venueCode}", produces = "application/json")
    public ResponseEntity<Venue> getVenue(@PathVariable Long venueCode) {
        try {
            Venue venue = venueService.getVenue(venueCode);
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

    @PutMapping(path = "/{venueCode}",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Venue> updateVenue(@PathVariable Long venueCode,
                                           @RequestBody Venue updatedVenue) {
        Venue retrievedVenue = venueService.getVenue(venueCode);
        if (retrievedVenue != null) {
            retrievedVenue.setVenueName(updatedVenue.getVenueName());
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

    @DeleteMapping(path = "/{venueCode}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long venueCode) {
        try {
            return new ResponseEntity<>(venueService.deleteVenue(venueCode) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

}
