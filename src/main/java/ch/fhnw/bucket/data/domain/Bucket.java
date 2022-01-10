package ch.fhnw.bucket.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

//Each bucket is a category for similar bucket items.
@Entity
public class Bucket {
    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty(message = "Please name the category.")
    private String name;

    //In hex code
    private String color;

    //url to image
    private String icon;

    //One avatar holds many buckets
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Avatar avatar;

    //One bucket has many bucket items
    @OneToMany(mappedBy = "bucket", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BucketItem> bucketItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public List<BucketItem> getBucketItems() {
        return bucketItems;
    }

    public void setBucketItems(List<BucketItem> bucketItems) {
        this.bucketItems = bucketItems;
    }

    /**
    Handle referential integrity constraint for 1:n relationship between Bucket and BucketItem

    If a Bucket is removed, the references to this Bucket must be removed from all BucketItems.
    This is not required in BucketItem as it is the owner of the relationship (as indicated by "mappedBy" in this class
    for List<BucketItem> bucketItems.
     */
    @PreRemove
    private void removeBucketFromBucketItems() {
        for (BucketItem bucketItem : this.bucketItems) {
            bucketItem.setBucket(null);
        }
    }
}


