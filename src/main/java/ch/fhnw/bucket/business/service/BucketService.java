package ch.fhnw.bucket.business.service;

import ch.fhnw.bucket.data.domain.Avatar;
import ch.fhnw.bucket.data.domain.Bucket;
import ch.fhnw.bucket.data.repository.AvatarRepository;
import ch.fhnw.bucket.data.repository.BucketRepository;
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
    private AvatarRepository avatarRepository;

    @Autowired
    private AvatarService avatarService;

    public Bucket saveBucket(@Valid Bucket bucket) throws Exception {
        try {
            //Assign current avatar to bucket
            bucket.setAvatar(avatarService.getCurrentAvatar());

            return bucketRepository.save(bucket);

        } catch (Exception e) {
            throw new Exception("No bucket found.");
        }
    }

    public Bucket updateBucket(@Valid Bucket bucket) throws Exception {
        //Check if bucket with given id is already present
        //Only buckets with valid id are updated.
        if (!bucketRepository.findById(bucket.getId()).isPresent()) {
            throw new Exception("No bucket with ID " + bucket.getId() + " found.");
        }
        //Call regular save method
        return saveBucket(bucket);
    }

    public Bucket findBucketByIdAndCurrentAvatar(Long bucketId) throws Exception {
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
