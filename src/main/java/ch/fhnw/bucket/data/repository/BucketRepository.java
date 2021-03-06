package ch.fhnw.bucket.data.repository;

import ch.fhnw.bucket.data.domain.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, Long> {
	List<Bucket> findByAvatarId(Long avatarId);
    List<Bucket> findByIdAndAvatarId(Long bucketId, Long avatarId);
    Bucket findBucketByIdAndAvatarId(Long bucketId, Long avatarId);
}