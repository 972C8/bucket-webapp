package ch.fhnw.acrm.business.service;

import ch.fhnw.acrm.data.domain.Bucket;
import ch.fhnw.acrm.data.repository.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class BucketService {
    @Autowired
    private BucketRepository bucketRepository;
    @Autowired
    private AvatarService avatarService;

    public Bucket saveBucket(@Valid Bucket bucket) {
        //Assign new bucket item to current user
        if (bucket.getAvatar() == null) {
            bucket.setAvatar(avatarService.getCurrentAvatar());
        }
        return bucketRepository.save(bucket);
    }

    //TODO: Fix bug where buckets are overwritten with parameter bucket instead of updated
    public Bucket updateBucket(@Valid Bucket bucket) throws Exception {
        //Check if bucket with given id is already present
        //Only buckets with valid id are updated.
        if (!bucketRepository.findById(bucket.getId()).isPresent()) {
            throw new Exception("No bucket with ID " + bucket.getId() + " found.");
        }
        return bucketRepository.save(bucket);
    }

    public Bucket findBucketById(Long bucketId) throws Exception {
        List<Bucket> bucketList = bucketRepository.findByIdAndAvatarId(bucketId, avatarService.getCurrentAvatar().getId());
        if (bucketList.isEmpty()) {
            throw new Exception("No bucket with ID " + bucketId + " found.");
        }
        return bucketList.get(0);
    }

    public void deleteBucket(Long bucketId) {
        bucketRepository.deleteById(bucketId);
    }

    public List<Bucket> findAllBuckets() {
        return bucketRepository.findByAvatarId(avatarService.getCurrentAvatar().getId());
    }
}
