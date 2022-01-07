package ch.fhnw.bucket.business.service;

import ch.fhnw.bucket.data.domain.*;
import ch.fhnw.bucket.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class BucketItemService {
    @Autowired
    private BucketItemRepository bucketItemRepository;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private BucketRepository bucketRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private AvatarService avatarService;

    /*
    Save BucketItem and assign referenced objects (avatar, bucket, image, label) based on provided id in JSON using a proxy

    Proxy logic adapted from:
    https://github.com/AnghelLeonard/Hibernate-SpringBoot/tree/master/HibernateSpringBootPopulatingChildViaProxy
     */
    public BucketItem saveBucketItem(@Valid BucketItem bucketItem) throws Exception {
        //Logic to store referenced objects by provided id from JSON
        try {
            Avatar currentAvatar = avatarService.getCurrentAvatar();
            Avatar proxy = null;

            //Add referenced avatar to bucket item that is saved
            if (bucketItem.getAvatar() != null) {
                //When creating a new bucket item, store the referenced avatar id as object
                Long avatarId = bucketItem.getAvatar().getId();

                //Find avatar object by id from provided RequestBody
                proxy = avatarRepository.findAvatarById(avatarId);
            }

            //Assign new bucket item to provided avatar. As fallback, assign it to the current avatar
            if (proxy == null) {
                bucketItem.setAvatar(currentAvatar);
            } else {
                bucketItem.setAvatar(proxy);
            }

            //Add referenced bucket
            if (bucketItem.getBucket() != null) {
                //When creating a new bucket item, store the referenced bucket id as object
                Long bucketId = bucketItem.getBucket().getId();

                //Find avatar object by id from provided RequestBody
                Bucket proxyBucketItem = bucketRepository.findBucketByIdAndAvatarId(bucketId, currentAvatar.getId());

                bucketItem.setBucket(proxyBucketItem);
            }

            //Add referenced image
            if (bucketItem.getImage() != null) {
                //When creating a new bucket item, store the referenced bucket id as object
                Long imageId = bucketItem.getImage().getId();

                //Find avatar object by id from provided RequestBody
                Image proxyImage = imageRepository.findImageById(imageId);

                bucketItem.setImage(proxyImage);
            }

            //Add referenced labels
            if (bucketItem.getLabels() != null) {
                //When creating a new bucket item, store the referenced labels as objects
                List<Label> proxyLabel = new ArrayList<Label>();

                //Find list of labels by id from provided RequestBody
                for (Label label : bucketItem.getLabels()) {
                    Label foundLabel = labelRepository.findLabelByIdAndAvatarId(label.getId(), avatarService.getCurrentAvatar().getId());
                    proxyLabel.add(foundLabel);
                }

                bucketItem.setLabels(proxyLabel);
            }

            return bucketItemRepository.save(bucketItem);
        } catch (Exception e) {
            throw new Exception("No bucket found.");
        }
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
