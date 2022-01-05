package ch.fhnw.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

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

    //One bucketItem holds many labels
    @ManyToOne
    private BucketItem bucketItem;

    //One avatar holds many labels
    //labels must be assigned to an avatar when created
    @ManyToOne
    private Avatar avatar;

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

    public BucketItem getBucketItem() {
        return bucketItem;
    }

    public void setBucketItem(BucketItem bucketItem) {
        this.bucketItem = bucketItem;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
}


