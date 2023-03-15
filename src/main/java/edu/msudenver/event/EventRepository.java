package edu.msudenver.event;

import edu.msudenver.venue.Venue;
import edu.msudenver.venue.VenueId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, EventId> {
}
