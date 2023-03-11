package edu.msudenver.city;

import edu.msudenver.country.Country;
import edu.msudenver.country.CountryRepository;
import edu.msudenver.country.CountryService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CityService.class)
public class CityServiceTest {

    @MockBean
    private CityRepository cityRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @Autowired
    private CityService cityService;

    @BeforeEach
    public void setup() {
        cityService.entityManager = entityManager;
    }

    @Test
    public void testGetCities() throws Exception {
        City testCity = new City();
        testCity.setName("Denver");
        testCity.setCountryCode("us");
        testCity.setPostalCode("80204");

        Mockito.when(cityRepository.findAll()).thenReturn(Arrays.asList(testCity));

        List<City> cities = cityService.getCities();
        assertEquals(1, cities.size());
        assertEquals("Denver", cities
                .get(0)
                .getName());
    }

    @Test
    public void testGetCity() throws Exception {
        City testCity = new City();
        testCity.setName("Denver");
        testCity.setCountryCode("us");
        testCity.setPostalCode("80204");

        CityId testCityId = new CityId();
        testCityId.setCountryCode("us");
        testCityId.setPostalCode("80204");

        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.of(testCity));
        City city = cityService.getCity(testCityId);
        assertEquals("Denver", city.getName());
   }

    @Test
    public void testGetCityNotFound() throws Exception {
        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertEquals(null,
                //need to find a way to return "not found" with cityId object
                //Stack provided this suggestion, let's give it a shot
                cityService.getCity(null));
    }

    @Test
    public void testSaveCity() throws Exception {
        City albuquerque = new City();
        albuquerque.setName("Albuquerque");
        albuquerque.setCountryCode("us");
        albuquerque.setPostalCode("87106");

        Mockito.when(cityRepository.saveAndFlush(Mockito.any())).thenReturn(albuquerque);
        Mockito.when(cityRepository.save(Mockito.any())).thenReturn(albuquerque);

        assertEquals(albuquerque, cityService.saveCity(albuquerque));
    }

    //TO-DO
    @Test
    public void testSaveCountryBadRequest() throws Exception {
        City badAbq = new City();
        badAbq.setName("Albuquerque");

        Mockito.when(cityRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(cityRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cityService.saveCity(badAbq);
        });
    }

    @Test
    public void testDeleteCity() throws Exception {
        City albuquerque = new City();
        albuquerque.setName("Albuquerque");
        albuquerque.setCountryCode("us");
        albuquerque.setPostalCode("87106");

        CityId abqId = new CityId();
        abqId.setCountryCode("us");
        abqId.setPostalCode("87106");

        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.of(albuquerque));
        Mockito.when(cityRepository.existsById(Mockito.any())).thenReturn(true);

        assertTrue(cityService.deleteCity(abqId));
    }

    @Test
    public void testDeleteCityNotFound() throws Exception {
        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(cityRepository.existsById(Mockito.any())).thenReturn(false);
        Mockito.doThrow(IllegalArgumentException.class)
                .when(cityRepository)
                .deleteById(Mockito.any());
        //same here, how to return "not found"?
        assertFalse(cityService.deleteCity(null));
    }
}
