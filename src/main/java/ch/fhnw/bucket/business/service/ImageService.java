package ch.fhnw.bucket.business.service;

import ch.fhnw.bucket.data.domain.Avatar;
import ch.fhnw.bucket.data.domain.BucketItem;
import ch.fhnw.bucket.data.domain.Image;
import ch.fhnw.bucket.data.repository.AvatarRepository;
import ch.fhnw.bucket.data.repository.BucketItemRepository;
import ch.fhnw.bucket.data.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Objects;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private BucketItemRepository bucketItemRepository;

    @Autowired
    private AvatarService avatarService;

    /*
    Save image in database as byte and add current avatar as owner of image.
     */
    public Image saveImage(MultipartFile img, Long avatarId, Long bucketItemId) throws Exception {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(img.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Image image = new Image(fileName, img.getContentType(), img.getBytes());

            //Add referenced Avatar to Image (using the provided ID) if it is a profile picture
            if (avatarId != null) {
                //Find avatar object by id from provided RequestBody
                Avatar proxy = avatarRepository.findAvatarById(avatarId);

                //Assign new bucket item to provided avatar
                if (proxy != null) {
                    image.setAvatar(proxy);
                }
            }

            //Add referenced BucketItem to Image by provided ID.
            if (bucketItemId != null) {
                BucketItem proxy = bucketItemRepository.findBucketItemByIdAndAvatarId(bucketItemId, avatarService.getCurrentAvatar().getId());

                if (proxy != null) {
                    image.setBucketItem(proxy);
                }
            }

            return imageRepository.save(image);
        } catch (Exception ex) {
            throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /*
    Get image from database by id
     */
    public Image getImage(Long imgId) throws FileNotFoundException {
        return imageRepository.findById(imgId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + imgId));
    }
}
