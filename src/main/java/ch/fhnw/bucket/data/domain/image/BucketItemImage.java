package ch.fhnw.bucket.data.domain.image;

import ch.fhnw.bucket.data.domain.BucketItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("BUCKETITEMIMAGE")
public class BucketItemImage extends AbstractImage {

    public BucketItemImage() {
        super();
    }

    public BucketItemImage(String fileName, String fileType, byte[] data) {
        super(fileName, fileType, data);
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
}
