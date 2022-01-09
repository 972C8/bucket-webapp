package ch.fhnw.bucket.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Takes a location in String format and uses the Google Maps API to geocode the location into latitude (lat)
 * and longitude (lng).
 */
@Entity
public class Location {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The location provided in text format.
     * Used to generate the location of lat and lng.
     */
    private String address;

    //The addresses can be attached to this url to get a functioning google maps url.
    private final String googleMapsUrl = "https://www.google.com/maps/place/";

    //One location can be used by many bucket items
    @OneToMany(mappedBy = "location", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<BucketItem> bucketItems;

    public Location() {
    }

    /**
     * Create Location by providing location in text formation, which is geocoded to lat and lng with the google maps api
     */
    public Location(String address) {
        setAddress(address);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        //Store the UrlEncoded address
        this.address = encodeValue(address);
    }

    /**
     * Method to encode a string value using `UTF-8` encoding scheme
     * <p>
     * Necessary to adhere to URL structure.
     */
    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public String getGoogleMapsUrl() {
        return googleMapsUrl;
    }

    public List<BucketItem> getBucketItems() {
        return bucketItems;
    }

    public void setBucketItems(List<BucketItem> bucketItems) {
        this.bucketItems = bucketItems;
    }

    /**
     * Handle referential integrity constraint for 1:n relationship between Location and BucketItem
     * <p>
     * If a Location is removed, the references to this Location must be removed from all BucketItems.
     * This is not required in BucketItem as it is the owner of the relationship (as indicated by "mappedBy" in this class
     * for List<BucketItem> bucketItems.
     */
    @PreRemove
    private void removeLocationFromBucketItems() {
        for (BucketItem bucketItem : this.bucketItems) {
            bucketItem.setLocation(null);
        }
    }
}
