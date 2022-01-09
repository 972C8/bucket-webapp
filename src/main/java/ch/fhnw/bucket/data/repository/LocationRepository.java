package ch.fhnw.bucket.data.repository;

import ch.fhnw.bucket.data.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAll();
    Location findLocationById(Long locationId);
}