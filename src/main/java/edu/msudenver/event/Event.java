package edu.msudenver.event;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import edu.msudenver.venue.Venue;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "event_id", columnDefinition = "SERIAL")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long eventId;

    @Column(name = "title", columnDefinition = "text")
    @NotNull(message = "title cannot be null")
    private String eventTitle;
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "starts", columnDefinition = "timestamp")
    private Timestamp eventStart;

    @Column(name = "ends", columnDefinition = "timestamp")
    private Timestamp eventEnd;

    @Id
    @Column(name = "venue_id", columnDefinition = "integer")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long eventVenue;

    @Type(type = "string-array")
    @Column(name = "colors", columnDefinition = "text[]")
    private String[] eventColors; //may be wrong data type

    @ManyToOne()
    @JoinColumns({
            @JoinColumn(name = "venue_id", referencedColumnName = "venue_id", insertable = false, updatable = false),
            @JoinColumn(name = "country_code", referencedColumnName = "country_code", insertable = false, updatable = false),
            @JoinColumn(name = "postal_code", referencedColumnName = "postal_code", insertable = false, updatable = false)
    })
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Venue venue;


    public Event() {
    }

    public Event(Long eventId,
                 String eventTitle,
                 Timestamp eventStart,
                 Timestamp eventEnd,
                 Long eventVenue,
                 String[] eventColors) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventVenue = eventVenue;
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

    public Long getEventVenue() { return eventVenue; }

    public void setEventVenue(Long eventVenue) { this.eventVenue = eventVenue; }

    public String[] getEventColors() { return eventColors; }

    public void setEventColors(String[] eventColors) { this.eventColors = eventColors; }

    public Venue getVenue() { return venue; }

    public void setVenue(Venue venue) { this.venue = venue; }
}
