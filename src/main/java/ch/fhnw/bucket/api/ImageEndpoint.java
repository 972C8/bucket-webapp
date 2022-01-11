package ch.fhnw.bucket.api;

import ch.fhnw.bucket.business.service.ImageService;
import ch.fhnw.bucket.data.domain.image.BucketItemImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api")
public class ImageEndpoint {
    @Autowired
    private ImageService imageService;

    /**
     * Save the uploaded image as the image of the provided bucket item id
     */
    @PostMapping("/bucket-items/images")
    public ResponseEntity<BucketItemImage> uploadImage(@RequestParam(value = "image") MultipartFile image) {
        try {
            BucketItemImage item = imageService.saveBucketItemImage(image);

            return ResponseEntity.accepted().body(item);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    //TODO: upload profile picture
    /**
     * Save the uploaded image as the profile picture of the current avatar
     */
    /*
    @PostMapping("/avatars/profile-picture")
    public ResponseEntity<ProfilePicture> uploadProfilePicture(@RequestParam(value = "image") MultipartFile image) {
        try {

            //ProfilePicture file = imageService.uploadAvatarProfilePicture(image);

            return ResponseEntity.accepted().body(file);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    @GetMapping("/avatars/profile-picture")
    public ResponseEntity<Resource> getProfilePicture() {
        try {
            // Load file as Resource
            ProfilePicture imageFile = imageService.getCurrentAvatarProfilePicture();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(imageFile.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageFile.getFileName() + "\"")
                    .body(new ByteArrayResource(imageFile.getData()));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    */

    /**
     * GET the bucket item image by id
     */
    @GetMapping("/bucket-items/images/{bucketItemId}")
    public ResponseEntity<Resource> getBucketItemImage(@PathVariable(value = "bucketItemId") String bucketItemId) {
        try {
            // Load file as Resource
            BucketItemImage imageFile = imageService.loadBucketItemImage(Long.parseLong(bucketItemId));

            //Create resource from imageFile
            Resource resource = imageService.loadResourceFromBucketItemImage(imageFile);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(imageFile.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageFile.getFileName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}