package edu.msudenver.venue;

import edu.msudenver.city.City;
import edu.msudenver.city.CityId;
import edu.msudenver.city.CityRepository;
import edu.msudenver.city.CityService;
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
@WebMvcTest(value = VenueService.class)
public class VenueServiceTest {

    @MockBean
    private VenueRepository venueRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @Autowired
    private VenueService venueService;

    @BeforeEach
    public void setup() {
        venueService.entityManager = entityManager;
    }

    @Test
    public void testGetVenues() throws Exception {
        Venue missionBallroom = new Venue();
        missionBallroom.setVenueId(4L);
        missionBallroom.setVenueName("Mission Ballroom");
        missionBallroom.setVenueAddress("4242 Wynkoop St");
        missionBallroom.setVenueType("public");
        missionBallroom.setCountryCode("us");
        missionBallroom.setPostalCode("80204");
        missionBallroom.setActive(true);

        Mockito.when(venueRepository.findAll()).thenReturn(Arrays.asList(missionBallroom));

        List<Venue> venues = venueService.getVenues();
        assertEquals(1, venues.size());
        assertEquals("Mission Ballroom", venues
                .get(0)
                .getVenueName());
    }

    @Test
    public void testGetVenue() throws Exception {
        Venue testVenue = new Venue();
        testVenue.setVenueId(4L);
        testVenue.setVenueName("Mission Ballroom");
        testVenue.setVenueAddress("4242 Wynkoop St");
        testVenue.setVenueType("public");
        testVenue.setCountryCode("us");
        testVenue.setPostalCode("80204");
        testVenue.setActive(true);

        VenueId testVenueId = new VenueId();
        testVenueId.setVenueCode(4L);
        testVenueId.setCountryCode("us");
        testVenueId.setPostalCode("80204");

        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.of(testVenue));
        Venue venue = venueService.getVenue(testVenueId);
        assertEquals("Mission Ballroom", venue.getVenueName());
   }
    @Test
    public void testGetVenueNotFound() throws Exception {
        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertEquals(null,
                //need to find a way to return "not found" with cityId object
                //Stack provided this suggestion, let's give it a shot
                venueService.getVenue(null));
    }

    @Test
    public void testSaveVenue() throws Exception {
        Venue testVenue = new Venue();
        testVenue.setVenueId(4L);
        testVenue.setVenueName("Mission Ballroom");
        testVenue.setVenueAddress("4242 Wynkoop St");
        testVenue.setVenueType("public");
        testVenue.setCountryCode("us");
        testVenue.setPostalCode("80204");
        testVenue.setActive(true);

        Mockito.when(venueRepository.saveAndFlush(Mockito.any())).thenReturn(testVenue);
        Mockito.when(venueRepository.save(Mockito.any())).thenReturn(testVenue);

        assertEquals(testVenue, venueService.saveVenue(testVenue));
    }

    @Test
    public void testSaveVenueBadRequest() throws Exception {
        Venue testVenue = new Venue();
        testVenue.setVenueId(4L);
        testVenue.setVenueName("Mission Ballroom");
        testVenue.setVenueAddress("4242 Wynkoop St");
        testVenue.setVenueType("public");
        testVenue.setCountryCode("us");
        testVenue.setPostalCode("80204");
        testVenue.setActive(true);

        Mockito.when(venueRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(venueRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            venueService.saveVenue(testVenue);
        });
    }

    @Test
    public void testDeleteVenue() throws Exception {
        Venue testVenue = new Venue();
        testVenue.setVenueId(4L);
        testVenue.setVenueName("Mission Ballroom");
        testVenue.setVenueAddress("4242 Wynkoop St");
        testVenue.setVenueType("public");
        testVenue.setCountryCode("us");
        testVenue.setPostalCode("80204");
        testVenue.setActive(true);

        VenueId testVenueId = new VenueId();
        testVenueId.setVenueCode(4L);
        testVenueId.setCountryCode("us");
        testVenueId.setPostalCode("87106");

        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.of(testVenue));
        Mockito.when(venueRepository.existsById(Mockito.any())).thenReturn(true);

        assertTrue(venueService.deleteVenue(testVenueId));
    }

    @Test
    public void testDeleteVenueNotFound() throws Exception {
        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(venueRepository.existsById(Mockito.any())).thenReturn(false);
        Mockito.doThrow(IllegalArgumentException.class)
                .when(venueRepository)
                .deleteById(Mockito.any());
        assertFalse(venueService.deleteVenue(null));
    }
}
