package ch.fhnw.acrm.business.service;

import ch.fhnw.acrm.data.domain.BucketItem;
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

    public BucketItem editBucketItem(@Valid BucketItem bucketItem) {
        //Assign to current avatar if not already set
        if (bucketItem.getAvatar() == null) {
            bucketItem.setAvatar(avatarService.getCurrentAvatar());
        }
        return bucketRepository.save(bucketItem);
    }

    public BucketItem findBucketItemById(Long itemId) throws Exception {
        List<BucketItem> bucketItemsList = bucketRepository.findByIdAndAvatarId(itemId, avatarService.getCurrentAvatar().getId());
        if (bucketItemsList.isEmpty()) {
            throw new Exception("No bucket item with ID " + itemId + " found.");
        }
        return bucketItemsList.get(0);
    }

    public void deleteBucketItem(Long itemId) {
        bucketRepository.deleteById(itemId);
    }

    public List<BucketItem> findAllBucketItemsByAvatar(long avatarId) {
        //TODO: Automatically use current user?
        //return bucketRepository.findByAvatarId(avatarService.getCurrentAvatar().getId());
        return bucketRepository.findByAvatarId(avatarId);
    }

}
