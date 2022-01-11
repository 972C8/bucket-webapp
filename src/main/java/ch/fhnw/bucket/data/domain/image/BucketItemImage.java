package ch.fhnw.bucket.data.domain.image;

import ch.fhnw.bucket.data.domain.BucketItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@DiscriminatorValue("BUCKETITEMIMAGE")
public class BucketItemImage extends AbstractImage {

    public BucketItemImage() {
        super();
    }

    public BucketItemImage(String fileName) {
        super(fileName);
    }

    public BucketItemImage(String fileName, String fileUrl, String fileType) {
        super(fileName, fileUrl, fileType);
    }

    //The image is assigned to a bucket item
    @OneToOne(mappedBy = "image", fetch = FetchType.EAGER)
    @JsonIgnore
    private BucketItem bucketItem;

    public BucketItem getBucketItem() {
        return bucketItem;
    }

    public void setBucketItem(BucketItem bucketItem) {
        this.bucketItem = bucketItem;
    }

    /**
    Handle referential integrity constraint for 1:1 relationship between Image and BucketItem

    If a Image is removed, the references to this Image must be removed from all BucketItems.
    This is not required in BucketItem as it is the owner of the relationship (as indicated by "mappedBy" in this class
    for List<BucketItem> bucketItem.
     */
    @PreRemove
    private void removeImageFromBucketItem() {
        this.bucketItem.setImage(null);
    }
}
