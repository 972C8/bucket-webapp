package ch.fhnw.bucket.data.repository;

import ch.fhnw.bucket.data.domain.image.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    ProfilePicture findProfilePictureByIdAndAvatarId(Long pictureId, Long avatarId);
}