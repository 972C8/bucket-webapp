package ch.fhnw.bucket.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
public class Label {

    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty(message = "Please name the label.")
    private String name;

    //In hex code
    @NotEmpty(message = "Please define the color.")
    private String color;

    //One avatar holds many labels
    //labels must be assigned to an avatar when created
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Avatar avatar;

    @ManyToMany(mappedBy = "labels", fetch = FetchType.LAZY)
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

    /*
    Handle referential integrity constraint for n:n relationship between Label and BucketItem

    If a Label is removed, the references to this Label must be removed from all BucketItems.
    This is not required in BucketItem as it is the owner of the relationship (as indicated by "mappedBy" in this class
    for List<BucketItem> bucketItems.
     */
    @PreRemove
    private void removeLabelsFromBucketItems() {
        for (BucketItem bucketItem : this.bucketItems) {
            bucketItem.getLabels().remove(this);
        }
    }
}


