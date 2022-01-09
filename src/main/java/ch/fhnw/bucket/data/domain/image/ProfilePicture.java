package ch.fhnw.bucket.data.domain.image;

import ch.fhnw.bucket.data.domain.Avatar;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

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
    @OneToOne(mappedBy = "profilePicture", fetch = FetchType.EAGER)
    @JsonIgnore
    private Avatar avatar;

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    /*
    Handle referential integrity constraint for 1:1 relationship between Image and Avatar

    If a Image is removed, the references to this Image must be removed from the Avatar.
    This is not required in Avatar as it is the owner of the relationship (as indicated by "mappedBy" in this class
    for Avatar avatar.
     */
    @PreRemove
    private void removeProfilePictureFromAvatar() {
        this.avatar.setProfilePicture(null);
    }
}
