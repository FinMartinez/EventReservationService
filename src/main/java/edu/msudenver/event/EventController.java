package edu.msudenver.event;

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

    @GetMapping(path = "/{eventCode}", produces = "application/json")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventCode) {
        try {
            //EventId eventId = new EventId(eventCode, venueCode);
            Event event = eventService.getEvent(eventCode);
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

    @PutMapping(path = "/{eventCode}",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventCode,
                                             @RequestBody Event updatedEvent) {
        Event retrievedEvent = eventService.getEvent(eventCode);
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

    @DeleteMapping(path = "/{eventCode}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventCode) {
        try {

            return new ResponseEntity<>(eventService.deleteEvent(eventCode) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

}
