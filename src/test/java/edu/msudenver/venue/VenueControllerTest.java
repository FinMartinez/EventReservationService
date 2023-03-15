package edu.msudenver.venue;

import edu.msudenver.city.City;
import edu.msudenver.city.CityController;
import edu.msudenver.city.CityService;
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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = VenueController.class)
public class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VenueService venueService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testGetVenues() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/venues/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Venue testVenue = new Venue();
        testVenue.setVenueId(4L);
        testVenue.setVenueName("Mission Ballroom");
        testVenue.setVenueAddress("4242 Wynkoop St");
        testVenue.setVenueType("public");
        testVenue.setCountryCode("us");
        testVenue.setPostalCode("80204");
        testVenue.setActive(true);

        Mockito.when(venueService.getVenues()).thenReturn(Arrays.asList(testVenue));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Mission Ballroom"));
    }

    @Test
    public void testGetVenue() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/venues/id/us/80204/")//what needs to go for venue id? Just this?
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Venue testVenue = new Venue();
        testVenue.setVenueId(4L); //see above
        testVenue.setVenueName("Mission Ballroom");
        testVenue.setVenueAddress("4242 Wynkoop St");
        testVenue.setVenueType("public");
        testVenue.setCountryCode("us");
        testVenue.setPostalCode("80204");
        testVenue.setActive(true);

        Mockito.when(venueService.getVenue(Mockito.any())).thenReturn(testVenue);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Mission Ballroom"));
    }

    @Test
    public void testGetVenueNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/venues/404") //be careful about this with venues. Have the controller accept
                                                      // long type and change this to a number, 404 I think.
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(venueService.getVenue(Mockito.any())).thenReturn(null);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testCreateVenue() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/venues/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"venueId\":\"4\", " +
                        "\"countryCode\":\"us\", " +
                        "\"postalCode\":\"80204\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Venue testVenue = new Venue();
        testVenue.setVenueId(4L);
        testVenue.setVenueName("Mission Ballroom");
        testVenue.setVenueAddress("4242 Wynkoop St");
        testVenue.setVenueType("public");
        testVenue.setCountryCode("us");
        testVenue.setPostalCode("80204");
        testVenue.setActive(true);
        Mockito.when(venueService.saveVenue(Mockito.any())).thenReturn(testVenue);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Mission Ballroom"));
    }

    @Test
    public void testCreateVenueBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/venues/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"venueName\": \"Mission Ballroom\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(venueService.saveVenue(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testUpdateVenue() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/venues/id/us/80204")//id can be anything you want
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"venueId\":\"4\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Venue testVenue = new Venue();
        testVenue.setVenueId(4L); //see above
        testVenue.setVenueName("Mission Ballroom");
        testVenue.setVenueAddress("4242 Wynkoop St");
        testVenue.setVenueType("public");
        testVenue.setCountryCode("us");
        testVenue.setPostalCode("80204");
        testVenue.setActive(true);
        Mockito.when(venueService.getVenue(Mockito.any())).thenReturn(testVenue);

        Venue testUpdated = new Venue();
        testUpdated.setVenueId(4L); //see above
        testUpdated.setVenueName("Mission Ballroom Updated");
        testUpdated.setVenueAddress("4242 Wynkoop St");
        testUpdated.setVenueType("public");
        testUpdated.setCountryCode("us");
        testUpdated.setPostalCode("80204");
        testUpdated.setActive(true);
        Mockito.when(venueService.saveVenue(Mockito.any())).thenReturn(testUpdated);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Mission Ballroom Updated"));
    }

    @Test
    public void testUpdateVenueNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/venues/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"venueId\":\"404\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(venueService.getVenue(Mockito.any())).thenReturn(null);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateVenueBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/venues/id/us/80204/") //add in venue id?
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"venueId\":\"4\", " +
                "\"venueName\":\"Mission Ballroom\", " +
                "\"venueAddress\":\"4242 Wynkoop St\", " +
                "\"venueType\":\"public\", " +
                "\"postalCode\":\"80204\", " +
                "\"countryCode\":\"us\", " +
                "\"active\": \"true\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Venue testVenue = new Venue();
        testVenue.setVenueId(4L); //see above
        testVenue.setVenueName("Mission Ballroom");
        testVenue.setVenueAddress("4242 Wynkoop St");
        testVenue.setVenueType("public");
        testVenue.setCountryCode("us");
        testVenue.setPostalCode("80204");
        testVenue.setActive(true);
        Mockito.when(venueService.getVenue(Mockito.any())).thenReturn(testVenue);

        Mockito.when(venueService.saveVenue(Mockito.any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testDeleteVenue() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/venues/id/us/80204/") //add in venue id
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(venueService.deleteVenue(Mockito.any())).thenReturn(true);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void testDeleteVenueNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/venues/404")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(venueService.deleteVenue(Mockito.any())).thenReturn(false);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

}
