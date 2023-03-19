package edu.msudenver.event;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
//import edu.msudenver.venue.Venue;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@TypeDefs({
        @TypeDef(
                name = "string-array",
                typeClass = StringArrayType.class
        )
})
@Entity
@Table(name = "events")
public class Event{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", columnDefinition = "SERIAL")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long eventId;

    @Column(name = "title", columnDefinition = "text")
    @NotNull(message = "title cannot be null")
    private String eventTitle;
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "starts", columnDefinition = "timestamp")
    private Timestamp eventStart;

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ends", columnDefinition = "timestamp")
    private Timestamp eventEnd;

    @Type(type = "string-array")
    @Column(name = "colors", columnDefinition = "text[]")
    private String[] eventColors; //may be wrong data type

    @Column(name = "venue_id", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long venueId;

    public Event() {
    }

    public Event(Long eventId,
                 String eventTitle,
                 Timestamp eventStart,
                 Timestamp eventEnd,
                 Long venueId,
                 String[] eventColors) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.venueId = venueId;
        this.eventColors = eventColors;
    }

    public Long getEventId() { return eventId; }

    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getEventTitle() { return eventTitle; }

    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public Timestamp getEventStart() { return eventStart; }

    public void setEventStart(Timestamp eventStart) { this.eventStart = eventStart; }

    public Timestamp getEventEnd() { return eventEnd; }

    public void setEventEnd(Timestamp eventEnd) { this.eventEnd = eventEnd; }

    public String[] getEventColors() { return eventColors; }

    public void setEventColors(String[] eventColors) { this.eventColors = eventColors; }

    public Long getVenueId() { return venueId; }

    public void setVenueId(Long venueId) { this.venueId = venueId; }
}
