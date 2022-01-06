package ch.fhnw.bucket.data.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;


@Entity
public class BucketItem {
    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty(message = "Please provide a valid title.")
    private String title;
    private String description;

    //When creating a new object, set created to current instant
    private final Instant created = Instant.now();

    //Date until bucket item should be accomplished
    private String dateToAccomplish;

    @ManyToOne
    //Referenced avatar is not returned in api requests
    //@JsonIgnore
    private Avatar avatar;

    //One bucket (category) holds many bucket items
    @ManyToOne
    private Bucket bucket;

    //one bucket item has one image
    @OneToOne(mappedBy = "bucketItem")
    private Image image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreated() {
        return created;
    }

    public String getDateToAccomplish() {
        return dateToAccomplish;
    }

    public void setDateToAccomplish(String dateToAccomplish) {
        this.dateToAccomplish = dateToAccomplish;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public Bucket getBucket() {
        return bucket;
    }

    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}