package ch.fhnw.bucket.data.domain.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/*
Based on single table inheritance of JPA:
https://en.wikibooks.org/wiki/Java_Persistence/Inheritance#Single_Table_Inheritance

In single table inheritance a single table is used to store all of the instances of the entire inheritance hierarchy.
The table will have a column for every attribute of every class in the hierarchy.
A discriminator column is used to determine which class the particular row belongs to,
each class in the hierarchy defines its own unique discriminator value.
 */
@Entity
@Inheritance
@DiscriminatorColumn(name="IMAGE_TYPE")
@Table(name="IMAGE")
public abstract class AbstractImage {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    private String fileName;

    @JsonIgnore
    private String fileType;

    //Check PostgreSQLDialectCustom.java to see custom implementation, making @Lob possible on Postgresql (heroku)
    //This is never an issue as the local storage is in-memory
    @Lob
    @JsonIgnore
    private byte[] data;

    public AbstractImage() {
    }

    public AbstractImage(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
