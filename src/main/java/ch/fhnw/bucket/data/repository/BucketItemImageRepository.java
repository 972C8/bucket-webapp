package ch.fhnw.bucket.data.repository;

import ch.fhnw.bucket.data.domain.image.BucketItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BucketItemImageRepository extends JpaRepository<BucketItemImage, Long> {
    BucketItemImage findImageById(Long imageId);
}