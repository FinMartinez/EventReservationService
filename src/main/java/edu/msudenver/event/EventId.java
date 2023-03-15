package edu.msudenver.event;

import java.io.Serializable;

public class EventId implements Serializable{

    private Long eventCode;
    private Long venueCode;

    public EventId() {
    }

    public EventId(Long eventCode, Long venueCode) {
        this.venueCode = venueCode;
        this.eventCode = eventCode;
    }

    public Long getEventCode() { return venueCode; }

    public void setEventCode(Long eventCode) { this.eventCode = eventCode; }

    public Long getVenueCode() {
        return eventCode;
    }

    public void setVenueCode(Long eventCode) {
        this.eventCode = eventCode;
    }
}
