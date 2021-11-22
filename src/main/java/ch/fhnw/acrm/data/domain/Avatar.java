package ch.fhnw.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
public class Avatar {

    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty(message = "Please provide a username.")
    private String username;
    @Email(message = "Please provide a valid e-mail.")
    @NotEmpty(message = "Please provide an e-mail.")
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // only create object property from JSON
    @NotEmpty(message = "Please provide a password.")
    private String password;

    @JsonIgnore
    private String role = "USER";

    @Transient // will not be stored in DB
    //stored as "true" or "false"
    private String remember;

    //One avatar has many bucket items
    @OneToMany(mappedBy = "avatar")
    @JsonIgnore
    private List<BucketItem> bucketItems;

    //One avatar has many buckets (categories)
    @OneToMany(mappedBy = "avatar")
    @JsonIgnore
    private List<Bucket> buckets;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        String transientPassword = this.password;
        this.password = null;
        return transientPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemember() {
        return remember;
    }

    public void setRemember(String remember) {
        this.remember = remember;
    }

    public String getRole() {
        return role;
    }

    public List<BucketItem> getBucketItems() {
        return bucketItems;
    }

    public void setBucketItems(List<BucketItem> bucketItems) {
        this.bucketItems = bucketItems;
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }
}
