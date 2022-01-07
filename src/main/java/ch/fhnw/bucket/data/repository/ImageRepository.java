package ch.fhnw.bucket.data.repository;

import ch.fhnw.bucket.data.domain.Bucket;
import ch.fhnw.bucket.data.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findImageById(Long imageId);
}