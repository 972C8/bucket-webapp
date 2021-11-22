package ch.fhnw.acrm.business.service;

import ch.fhnw.acrm.data.domain.Bucket;
import ch.fhnw.acrm.data.repository.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

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

}
