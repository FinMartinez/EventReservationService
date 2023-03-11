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
@WebMvcTest(value = CityController.class)
public class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testGetCities() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/cities/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        City testCity = new City();
        testCity.setName("Denver");
        testCity.setCountryCode("us");
        testCity.setPostalCode("80204");

        Mockito.when(cityService.getCities()).thenReturn(Arrays.asList(testCity));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Denver"));
    }

    @Test
    public void testGetCity() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/cities/us/80204")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        City testCity = new City();
        testCity.setName("Denver");
        testCity.setCountryCode("us");
        testCity.setPostalCode("80204");

        Mockito.when(cityService.getCity(Mockito.any())).thenReturn(testCity);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Denver"));
    }

    @Test
    public void testGetCityNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/countries/notfound") //be careful about this with venues. Have the controller accept
                                                      // long type and change this to a number, 404 I think.
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(cityService.getCity(Mockito.any())).thenReturn(null);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testCreateCity() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cities/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"countryCode\":\"ca\", \"postalCode\": \"80204\"}")
                .contentType(MediaType.APPLICATION_JSON);

        City denver = new City();
        denver.setName("Denver");
        denver.setCountryCode("us");
        denver.setPostalCode("80204");
        Mockito.when(cityService.saveCity(Mockito.any())).thenReturn(denver);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Denver"));
    }

    @Test
    public void testCreateCityBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cities/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"cityName\": \"Denver\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(cityService.saveCity(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testUpdateCity() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/cities/us/80204")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"countryCode\":\"us\", \"postalCode\":\"80204\", \"Name\": \"Denver Updated\"}")
                .contentType(MediaType.APPLICATION_JSON);

        City denver = new City();
        denver.setName("Denver");
        denver.setCountryCode("us");
        denver.setPostalCode("80204");
        Mockito.when(cityService.getCity(Mockito.any())).thenReturn(denver);

        City denverUpdated = new City();
        denverUpdated.setName("Denver Updated");
        denverUpdated.setCountryCode("us");
        Mockito.when(cityService.saveCity(Mockito.any())).thenReturn(denverUpdated);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Denver Updated"));
    }

    @Test
    public void testUpdateCityNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/cities/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"countryCode\": \"us\", \"postalCode\": \"80204\", \"Name\": \"notfound\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(cityService.getCity(Mockito.any())).thenReturn(null);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateCityBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/cities/us/80204")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"countryCode\":\"us\", \"postalCode\":\"80204\", \"Name\":\"Denver\"}")
                .contentType(MediaType.APPLICATION_JSON);

        City denver = new City();
        denver.setName("Denver");
        denver.setCountryCode("us");
        denver.setPostalCode("80204");
        Mockito.when(cityService.getCity(Mockito.any())).thenReturn(denver);

        Mockito.when(cityService.saveCity(Mockito.any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testDeleteCity() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/cities/us/80204")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(cityService.deleteCity(Mockito.any())).thenReturn(true);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void testDeleteCityNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/cities/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(cityService.deleteCity(Mockito.any())).thenReturn(false);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

}
