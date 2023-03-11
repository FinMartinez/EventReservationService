package edu.msudenver.city;

import edu.msudenver.country.Country;
import edu.msudenver.country.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    public List<City> getCities() {
        return cityRepository.findAll();
    }

    public City getCity(CityId cityId) {
        try {
            return cityRepository.findById(cityId).get();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public City saveCity(City city) {
        city = cityRepository.saveAndFlush(city);
        entityManager.refresh(city);
        return city;
    }
    public boolean deleteCity(CityId cityId) {
        try {
            if(cityRepository.existsById(cityId)) {
                cityRepository.deleteById(cityId);
                return true;
            }
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

        return false;
    }

}
