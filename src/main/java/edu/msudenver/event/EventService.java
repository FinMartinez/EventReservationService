package edu.msudenver.event;

import edu.msudenver.venue.Venue;
import edu.msudenver.venue.VenueId;
import edu.msudenver.venue.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Event getEvent(EventId eventId) {
        try {
            return eventRepository.findById(eventId).get();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Event saveEvent(Event event) {
        event = eventRepository.saveAndFlush(event);
        entityManager.refresh(event);
        return event;
    }
    public boolean deleteEvent(EventId eventId) {
        try {
            if(eventRepository.existsById(eventId)) {
                eventRepository.deleteById(eventId);
                return true;
            }
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

        return false;
    }

}
