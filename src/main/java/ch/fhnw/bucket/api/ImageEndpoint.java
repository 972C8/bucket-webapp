package ch.fhnw.bucket.api;

import ch.fhnw.bucket.business.service.ImageService;
import ch.fhnw.bucket.data.domain.image.BucketItemImage;
import ch.fhnw.bucket.data.domain.image.ProfilePicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api")
public class ImageEndpoint {
    @Autowired
    private ImageService imageService;

    /*
    Save the uploaded image as the image of the provided bucket item id
     */
    @PostMapping("/bucket-items/images")
    public ResponseEntity<BucketItemImage> uploadImage(
            @RequestParam(value = "image") MultipartFile image,
            @RequestParam(value = "bucketItem") Long bucketItemId) {
        try {
            BucketItemImage file = imageService.saveBucketItemImage(image, bucketItemId);

            //TODO: return Response?
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(file.getFileName())
                    .toUriString();

            //TODO: remove print. Url not valid!
            System.out.println(fileDownloadUri);

            return ResponseEntity.accepted().body(file);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    /*
    Save the uploaded image as the profile picture of the current avatar
     */
    @PostMapping("/avatars/profile-picture")
    public ResponseEntity<ProfilePicture> uploadImage(@RequestParam(value = "image") MultipartFile image) {
        try {
            ProfilePicture file = imageService.saveAvatarProfilePicture(image);

            return ResponseEntity.accepted().body(file);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    /*
    GET the image by id
     */
    //TODO: FIX GET for both ProfilePicture and BucketItem
    @GetMapping("/images/{imageId:.+}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId, HttpServletRequest request) {
        try {

            // Load file as Resource
            BucketItemImage imageFile = imageService.getImage(imageId);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(imageFile.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageFile.getFileName() + "\"")
                    .body(new ByteArrayResource(imageFile.getData()));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //TODO: UploadStream
    /*
    @PostMapping("/uploadMultipleFiles")
    public List < Response > uploadMultipleFiles(@RequestParam("images") MultipartFile[] images) {
        return Arrays.asList(images)
                .stream()
                .map(image -> uploadFile(image))
                .collect(Collectors.toList());
    }
     */
}