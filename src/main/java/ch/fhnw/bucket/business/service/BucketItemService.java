package ch.fhnw.bucket.business.service;

import ch.fhnw.bucket.data.domain.BucketItem;
import ch.fhnw.bucket.data.repository.BucketItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class BucketItemService {
    @Autowired
    private BucketItemRepository bucketItemRepository;
    @Autowired
    private AvatarService avatarService;

    public BucketItem saveBucketItem(@Valid BucketItem bucketItem) {
        //Assign new bucket item to current user
        if (bucketItem.getAvatar() == null) {
            bucketItem.setAvatar(avatarService.getCurrentAvatar());
        }
        return bucketItemRepository.save(bucketItem);
    }

    public BucketItem findBucketItemById(Long itemId) throws Exception {
        List<BucketItem> bucketItemsList = bucketItemRepository.findByIdAndAvatarId(itemId, avatarService.getCurrentAvatar().getId());
        if (bucketItemsList.isEmpty()) {
            throw new Exception("No bucket item with ID " + itemId + " found.");
        }
        return bucketItemsList.get(0);
    }

    public void deleteBucketItem(Long itemId) {
        bucketItemRepository.deleteById(itemId);
    }

    public List<BucketItem> findAllBucketItems() {
        return bucketItemRepository.findByAvatarId(avatarService.getCurrentAvatar().getId());
    }

}
