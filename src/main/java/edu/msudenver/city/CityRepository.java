package edu.msudenver.city;

import edu.msudenver.country.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, CityId> {
}
