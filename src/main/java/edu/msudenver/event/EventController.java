package edu.msudenver.event;

import edu.msudenver.venue.Venue;
import edu.msudenver.venue.VenueId;
import edu.msudenver.venue.VenueService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Event>> getEvents() {
        try {
            return ResponseEntity.ok(eventService.getEvents());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{eventCode}/{venueCode}", produces = "application/json")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventCode,
                                        @PathVariable Long venueCode) {
        try {
            EventId eventId = new EventId(eventCode, venueCode);
            Event event = eventService.getEvent(eventId);
            return new ResponseEntity<>(event, event == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        try {
            return new ResponseEntity<>(eventService.saveEvent(event), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/{venueCode}/{eventCode}",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Event> updateEvent(@PathVariable Long venueCode,
                                             @PathVariable Long eventCode,
                                             @RequestBody Event updatedEvent) {
        EventId eventId = new EventId(venueCode,eventCode);
        Event retrievedEvent = eventService.getEvent(eventId);
        if (retrievedEvent != null) {
            retrievedEvent.setEventTitle(updatedEvent.getEventTitle());
            try {
                return ResponseEntity.ok(eventService.saveEvent(retrievedEvent));
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{venueCode}/{eventCode}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long venueCode,
                                           @PathVariable Long eventCode) {
        try {
            EventId eventId = new EventId(venueCode, eventCode);
            return new ResponseEntity<>(eventService.deleteEvent(eventId) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

}
