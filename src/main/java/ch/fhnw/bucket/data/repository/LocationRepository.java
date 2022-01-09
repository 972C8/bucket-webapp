package ch.fhnw.bucket.data.repository;

import ch.fhnw.bucket.data.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findLocationById(Long locationId);
}