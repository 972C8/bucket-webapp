package ch.fhnw.bucket.api;

import ch.fhnw.bucket.business.service.LocationService;
import ch.fhnw.bucket.data.domain.Bucket;
import ch.fhnw.bucket.data.domain.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class LocationEndpoint {
    @Autowired
    private LocationService locationService;

    /**
     * Create a new location based on the address in text format
     */
    @PostMapping(path = "/locations")
    public ResponseEntity<Location> createLocation(@RequestParam(value = "address") String address) {
        try {
            Location geocode = locationService.saveLocation(address);
            return ResponseEntity.accepted().body(geocode);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    /**
     * Get location by id
     *
     * @param locationId
     * @return Optional<Location>
     */
    @GetMapping(path = "/locations/{locationId}", produces = "application/json")
    public ResponseEntity<Optional<Location>> getLocation(@PathVariable(value = "locationId") String locationId) {
        Optional<Location> location;
        try {
            location = locationService.findLocationByIdAndCurrentAvatar(Long.parseLong(locationId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(location);
    }

    @DeleteMapping(path = "/locations/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable(value = "locationId") String locationId) {
        try {
            locationService.deleteLocation(Long.parseLong(locationId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }

    /*
    Returns List<Location> of locations assigned to the given avatar
    */
    @GetMapping(path = "/locations", produces = "application/json")
    public List<Location> getLocationItems() {
        return locationService.findAllLocations();
    }
}