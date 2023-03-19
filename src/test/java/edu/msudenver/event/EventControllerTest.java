package edu.msudenver.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import static java.awt.Color.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testGetEvents() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/events/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = new Event();
        testEvent.setEventId(4L);
        testEvent.setEventTitle("Synthfest");
        testEvent.setEventStart(Timestamp.valueOf(LocalDateTime.now()));
        testEvent.setEventEnd(Timestamp.valueOf(LocalDateTime.now()));
        testEvent.setVenueId(4L);
        String[] colors = {"purple", "black", "orange"};
        testEvent.setEventColors(colors);


        Mockito.when(eventService.getEvents()).thenReturn(Arrays.asList(testEvent));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Synthfest"));
    }

    @Test
    public void testGetEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/events/4/")//what needs to go for venue id? Just this?
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = new Event();
        testEvent.setEventId(4L);
        testEvent.setEventTitle("Synthfest");
        testEvent.setEventStart(Timestamp.valueOf(LocalDateTime.now()));
        testEvent.setEventEnd(Timestamp.valueOf(LocalDateTime.now()));
        testEvent.setVenueId(4L);
        String[] colors = {"purple", "black", "orange"};
        testEvent.setEventColors(colors);

        Mockito.when(eventService.getEvent(Mockito.any())).thenReturn(testEvent);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Synthfest"));
    }

    @Test
    public void testGetEventNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/events/404") //be careful about this with venues. Have the controller accept
                                                      // long type and change this to a number, 404 I think.
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventService.getEvent(Mockito.any())).thenReturn(null);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testCreateEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/events/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"eventId\":\"4\",\"venueId\":\"4\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = new Event();
        testEvent.setEventId(4L);
        testEvent.setEventTitle("Synthfest");
        testEvent.setEventStart(Timestamp.valueOf("2023-03-17 17:30:00.000000"));
        testEvent.setEventEnd(Timestamp.valueOf("2023-03-18 01:30:00.000000"));
        testEvent.setVenueId(4L);
        String[] colors = {"purple", "black", "orange"};
        testEvent.setEventColors(colors);
        Mockito.when(eventService.saveEvent(Mockito.any())).thenReturn(testEvent);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Synthfest"));
    }
    @Test
    public void testCreateEventBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/events/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"eventId\": \"4\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventService.saveEvent(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testUpdateVenue() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/events/4/")//id can be anything you want
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"eventId\":\"4\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = new Event();
        testEvent.setEventId(4L);
        testEvent.setEventTitle("Synthfest");
        testEvent.setEventStart(Timestamp.valueOf("2023-03-17 17:30:00.000000"));
        testEvent.setEventEnd(Timestamp.valueOf("2023-03-18 01:30:00.000000"));
        testEvent.setVenueId(4L);
        String[] colors = {"purple", "black", "orange"};
        testEvent.setEventColors(colors);

        Mockito.when(eventService.getEvent(Mockito.any())).thenReturn(testEvent);

        Event testEventUpdated = new Event();
        testEventUpdated.setEventId(4L);
        testEventUpdated.setEventTitle("Synthfest Updated");
        testEventUpdated.setEventStart(Timestamp.valueOf("2023-03-17 17:30:00.000000"));
        testEventUpdated.setEventEnd(Timestamp.valueOf("2023-03-18 01:30:00.000000"));
        testEventUpdated.setVenueId(4L);
        String[] colorsUpated = {"purple", "black", "orange", "red"};
        testEventUpdated.setEventColors(colorsUpated);
        Mockito.when(eventService.saveEvent(Mockito.any())).thenReturn(testEventUpdated);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Synthfest Updated"));
    }

    @Test
    public void testUpdateEventNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/events/404")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"eventId\":\"404\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventService.getEvent(Mockito.any())).thenReturn(null);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateEventBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/events/4") //add in venue id?
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"eventId\":\"4\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = new Event();
        testEvent.setEventId(4L);
        testEvent.setEventTitle("Synthfest");
        testEvent.setEventStart(Timestamp.valueOf(LocalDateTime.now()));
        testEvent.setEventEnd(Timestamp.valueOf(LocalDateTime.now()));
        testEvent.setVenueId(4L);
        String[] colors = {"purple", "black", "orange"};
        testEvent.setEventColors(colors);
        Mockito.when(eventService.getEvent(Mockito.any())).thenReturn(testEvent);

        Mockito.when(eventService.saveEvent(Mockito.any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testDeleteEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/events/4/") //add in venue id
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventService.deleteEvent(Mockito.any())).thenReturn(true);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void testDeleteEventNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/events/404")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventService.deleteEvent(Mockito.any())).thenReturn(false);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }
}
