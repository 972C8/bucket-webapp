package ch.fhnw.bucket.business.service;

import ch.fhnw.bucket.data.domain.*;
import ch.fhnw.bucket.data.domain.image.BucketItemImage;
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
    private LocationRepository locationRepository;
    @Autowired
    private BucketRepository bucketRepository;
    @Autowired
    private BucketItemImageRepository bucketItemImageRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private AvatarService avatarService;

    /**
    Save BucketItem and assign referenced objects (avatar, bucket, image, label) based on provided id in JSON using a proxy

    Proxy logic adapted from:
    https://github.com/AnghelLeonard/Hibernate-SpringBoot/tree/master/HibernateSpringBootPopulatingChildViaProxy
     */
    public BucketItem saveBucketItem(@Valid BucketItem bucketItem) throws Exception {
        //Logic to store referenced objects by provided id from JSON
        try {
            Avatar currentAvatar = avatarService.getCurrentAvatar();

            //Assign bucket item to the current avatar
            bucketItem.setAvatar(currentAvatar);

            //Add referenced bucket
            if (bucketItem.getBucket() != null) {
                //When creating a new bucket item, store the referenced bucket id as object
                Long bucketId = bucketItem.getBucket().getId();

                //Find avatar object by id from provided RequestBody
                Bucket proxyBucket = bucketRepository.findBucketByIdAndAvatarId(bucketId, currentAvatar.getId());

                bucketItem.setBucket(proxyBucket);
            }

            //Add referenced image
            if (bucketItem.getImage() != null) {
                //When creating a new bucket item, store the referenced bucket id as object
                Long imageId = bucketItem.getImage().getId();

                //Find avatar object by id from provided RequestBody
                BucketItemImage proxyImage = bucketItemImageRepository.findBucketItemImageById(imageId);

                bucketItem.setImage(proxyImage);
            }

            //Add referenced labels
            if (bucketItem.getLabels() != null) {
                //When creating a new bucket item, store the referenced labels as objects
                List<Label> proxyLabel = new ArrayList<>();

                //Find list of labels by id from provided RequestBody
                for (Label label : bucketItem.getLabels()) {
                    Label foundLabel = labelRepository.findLabelByIdAndAvatarId(label.getId(), avatarService.getCurrentAvatar().getId());
                    if (foundLabel != null) {
                        proxyLabel.add(foundLabel);
                    }
                }

                bucketItem.setLabels(proxyLabel);
            }

            //Add referenced location
            if (bucketItem.getLocation() != null) {
                //When creating a new bucket item, store the referenced bucket id as object
                Long locationId = bucketItem.getLocation().getId();

                //Find avatar object by id from provided RequestBody
                Location proxyLocation = locationRepository.findLocationById(locationId);

                bucketItem.setLocation(proxyLocation);
            }

            return bucketItemRepository.save(bucketItem);
        } catch (Exception e) {
            throw new Exception("No bucket found.");
        }
    }

    public BucketItem findBucketItemByIdAndCurrentAvatar(Long itemId) throws Exception {
        List<BucketItem> bucketItemsList = bucketItemRepository.findByIdAndAvatarId(itemId, avatarService.getCurrentAvatar().getId());
        if (bucketItemsList.isEmpty()) {
            throw new Exception("No bucket item with ID " + itemId + " found.");
        }
        return bucketItemsList.get(0);
    }

    public void deleteBucketItem(Long itemId) {
        bucketItemRepository.deleteById(itemId);
    }

    /**
    Returns a list of BucketItems based on query params provided through the API

    Query params:bucketId, completed, labelId
     */
    public List<BucketItem> findAllBucketItems(Long bucketId, Boolean completed, Long labelId, Integer limit) {

        //The result based on the optional query params bucketId and completed
        List<BucketItem> bucketItems = bucketItemRepository.findByParams(avatarService.getCurrentAvatar().getId(), bucketId, completed);

        //Optional query param labelId must be checked. If labelid was provided, bucketItems must be checked
        // and adjusted accordingly. The newly filtered result is overrides bucketItems
        if (labelId != null) {
            //Override the result with the new list that matches the query param labelId
            bucketItems = filterBucketItemsByLabelId(bucketItems, labelId);
        }

        //Limit the result size based on query param limit
        if (limit != null && limit >= 0) {
            //No need to limit the result if the limit is bigger than the size
            if (limit < bucketItems.size()) {
                bucketItems = bucketItems.subList(0, limit);
            }
        }
        return bucketItems;
    }

    /**
    Filter the result of findAllBucketItems() by query param labelId
     */
    private List<BucketItem> filterBucketItemsByLabelId(List<BucketItem> bucketItems, Long labelId) {
        //The List<BucketItem> must be filtered based on the label provided
        List<BucketItem> result = new ArrayList<>();
        //Check each BucketItem
        for (BucketItem item : bucketItems) {
            //Check each label of the bucketItem
            for (Label label : item.getLabels()) {
                //Check if a label corresponds to the LabelId provided in the API
                if (label.getId().equals(labelId)) {
                    //The BucketItem was found to contain the labelId. Add to the results list
                    result.add(item);
                }
            }
        }
        return result;
    }

}
