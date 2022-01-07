package ch.fhnw.bucket.data.domain.image;

import ch.fhnw.bucket.data.domain.Avatar;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("PROFILEPICTURE")
public class ProfilePicture extends AbstractImage {

    public ProfilePicture() {
        super();
    }

    public ProfilePicture(String fileName, String fileType, byte[] data) {
        super(fileName, fileType, data);
    }

    //A profile picture is assigned to one avatar
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Avatar avatar;

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
}
