package ch.fhnw.bucket.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/*
Image will be stored as a byte array in the database
 */
@Entity
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    private String fileType;

    //One avatar has one profile picture (image)
    @OneToOne
    @JsonIgnore
    private Avatar avatar;

    //one bucket item has one image
    @OneToOne
    @JsonIgnore
    private BucketItem bucketItem;

    @Lob
    @JsonIgnore
    private byte[] data;

    public Image() {
    }

    public Image(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public BucketItem getBucketItem() {
        return bucketItem;
    }

    public void setBucketItem(BucketItem bucketItem) {
        this.bucketItem = bucketItem;
    }
}