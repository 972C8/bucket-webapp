package ch.fhnw.bucket.business.service;

import ch.fhnw.bucket.data.domain.Location;
import ch.fhnw.bucket.data.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;
import java.util.List;

@Service
@Validated
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public Location saveLocation(@Valid String address) {
        //Create a new location using the address
        Location location = new Location(address);

        return locationRepository.save(location);
    }

    public Optional<Location> findLocationByIdAndCurrentAvatar(Long locationId) throws Exception {
        Optional<Location> location = locationRepository.findById(locationId);
        if (!location.isPresent()) {
            throw new Exception("No label with ID " + locationId + " found.");
        }
        return location;
    }

    public void deleteLocation(Long locationId) {
        locationRepository.deleteById(locationId);
    }

    /**
    Returns List<Location> of locations assigned to the given avatar
    */
    public List<Location> findAllLocations() {
        return locationRepository.findAll();
    }

}
