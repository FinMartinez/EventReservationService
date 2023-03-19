package edu.msudenver.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = EventService.class)
public class EventServiceTest {

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @Autowired
    private EventService eventService;

    @BeforeEach
    public void setup() {
        eventService.entityManager = entityManager;
    }

    @Test
    public void testGetEvents() throws Exception {
        Event testEvent = new Event();
        testEvent.setEventId(4L);
        testEvent.setEventTitle("Synthfest");
        testEvent.setEventStart(LocalDateTime.now());
        testEvent.setEventEnd(LocalDateTime.now());
        testEvent.setVenueId(4L);
        String[] colors = {"purple", "black", "orange"};
        testEvent.setEventColors(colors);

        Mockito.when(eventRepository.findAll()).thenReturn(Arrays.asList(testEvent));

        List<Event> events = eventService.getEvents();
        assertEquals(1, events.size());
        assertEquals("Synthfest", events
                .get(0)
                .getEventTitle());
    }

    @Test
    public void testGetEvent() throws Exception {
        Event testEvent = new Event();
        testEvent.setEventId(4L);
        testEvent.setEventTitle("Synthfest");
        testEvent.setEventStart(LocalDateTime.now());
        testEvent.setEventEnd(LocalDateTime.now());
        testEvent.setVenueId(4L);
        String[] colors = {"purple", "black", "orange"};
        testEvent.setEventColors(colors);

        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.of(testEvent));
        Event event = eventService.getEvent(4L);
        assertEquals("Synthfest", event.getEventTitle());
   }

    @Test
    public void testGetEventNotFound() throws Exception {
        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertEquals(null,
                eventService.getEvent(null));
    }

    @Test
    public void testSaveEvent() throws Exception {
        Event testEvent = new Event();
        testEvent.setEventId(4L);
        testEvent.setEventTitle("Synthfest");
        testEvent.setEventStart(LocalDateTime.now());
        testEvent.setEventEnd(LocalDateTime.now());
        testEvent.setVenueId(4L);
        String[] colors = {"purple", "black", "orange"};
        testEvent.setEventColors(colors);

        Mockito.when(eventRepository.saveAndFlush(Mockito.any())).thenReturn(testEvent);
        Mockito.when(eventRepository.save(Mockito.any())).thenReturn(testEvent);

        assertEquals(testEvent, eventService.saveEvent(testEvent));
    }

    @Test
    public void testSaveEventBadRequest() throws Exception {
        Event testEvent = new Event();
        testEvent.setEventId(4L);
        testEvent.setEventTitle("Synthfest");
        testEvent.setEventStart(LocalDateTime.now());
        testEvent.setEventEnd(LocalDateTime.now());
        testEvent.setVenueId(4L);
        String[] colors = {"purple", "black", "orange"};
        testEvent.setEventColors(colors);

        Mockito.when(eventRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(eventRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            eventService.saveEvent(testEvent);
        });
    }
    @Test
    public void testDeleteEvent() throws Exception {
        Event testEvent = new Event();
        testEvent.setEventId(4L);
        testEvent.setEventTitle("Synthfest");
        testEvent.setEventStart(LocalDateTime.now());
        testEvent.setEventEnd(LocalDateTime.now());
        testEvent.setVenueId(4L);
        String[] colors = {"purple", "black", "orange"};
        testEvent.setEventColors(colors);

        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.of(testEvent));
        Mockito.when(eventRepository.existsById(Mockito.any())).thenReturn(true);

        assertTrue(eventService.deleteEvent(4L));
    }

    @Test
    public void testDeleteEventNotFound() throws Exception {
        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(eventRepository.existsById(Mockito.any())).thenReturn(false);
        Mockito.doThrow(IllegalArgumentException.class)
                .when(eventRepository)
                .deleteById(Mockito.any());
        assertFalse(eventService.deleteEvent(null));
    }
}
