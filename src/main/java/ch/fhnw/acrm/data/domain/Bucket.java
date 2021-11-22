package ch.fhnw.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

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
    @ManyToOne
    @JsonIgnore
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
}


