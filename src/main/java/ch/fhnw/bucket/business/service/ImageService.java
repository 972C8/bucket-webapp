package ch.fhnw.bucket.business.service;

import ch.fhnw.bucket.data.domain.BucketItem;
import ch.fhnw.bucket.data.domain.image.BucketItemImage;
import ch.fhnw.bucket.data.domain.image.ProfilePicture;
import ch.fhnw.bucket.data.repository.BucketItemRepository;
import ch.fhnw.bucket.data.repository.BucketItemImageRepository;
import ch.fhnw.bucket.data.repository.ProfilePictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Objects;

/*
Service to store image uploaded through API as byte in the database.
 */
@Service
public class ImageService {
    @Autowired
    private BucketItemImageRepository bucketItemImageRepository;
    @Autowired
    private ProfilePictureRepository profilePictureRepository;
    @Autowired
    private BucketItemRepository bucketItemRepository;

    @Autowired
    private AvatarService avatarService;

    /*
    Save image in database as byte and add current avatar as owner of image.
     */
    public BucketItemImage saveBucketItemImage(MultipartFile img, Long bucketItemId) throws Exception {
        try {
            //bucketItemId is required to upload a bucket item image
            if (bucketItemId == null) {
                throw new Exception("Storing a bucket item image requires a bucket item id.");
            }

            // Normalize file name
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(img.getOriginalFilename()));

            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
            }

            //Create the uploaded image object
            BucketItemImage image = new BucketItemImage(fileName, img.getContentType(), img.getBytes());

            //Add referenced BucketItem to Image by provided ID.
            //It is mandatory that a bucketItem can be referenced. If no entity is found, an exception is thrown
            BucketItem proxy = bucketItemRepository.findBucketItemByIdAndAvatarId(bucketItemId, avatarService.getCurrentAvatar().getId());
            if (proxy == null) {
                throw new Exception("Storing a bucket item image requires a bucket item id.");
            }
            image.setBucketItem(proxy);

            return bucketItemImageRepository.save(image);
        } catch (Exception ex) {
            throw new Exception("Could not store image.", ex);
        }
    }

    /*
    Create the new profile picture and assign it to the current user.
    If a profile picture already exists, it is overwritten and the old one is deleted.
     */
    public ProfilePicture uploadAvatarProfilePicture(MultipartFile img) throws Exception {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(img.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
            }

            ProfilePicture image = new ProfilePicture(fileName, img.getContentType(), img.getBytes());

            //Assign the current avatar to the created image
            image.setAvatar(avatarService.getCurrentAvatar());

            //Remove the current profile picture from the current avatar as it is overwritten with the new one
            if (avatarService.getCurrentAvatar().getProfilePicture() != null) {
                profilePictureRepository.delete(avatarService.getCurrentAvatar().getProfilePicture());
            }

            return profilePictureRepository.save(image);
        } catch (Exception ex) {
            throw new Exception("Could not store file " + fileName, ex);
        }
    }

    /*
Get image from database by id
 */
    public BucketItemImage getImage(Long imgId) throws FileNotFoundException {
        return bucketItemImageRepository.findById(imgId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + imgId));
    }

    /*
    Get profile picture of the current avatar
     */
    public ProfilePicture getCurrentAvatarProfilePicture() {
        return profilePictureRepository.findProfilePictureByAvatarId(avatarService.getCurrentAvatar().getId());
    }
}
