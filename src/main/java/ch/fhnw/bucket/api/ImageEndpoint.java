package ch.fhnw.bucket.api;

import ch.fhnw.bucket.business.service.ImageService;
import ch.fhnw.bucket.data.domain.Image;
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

    @PostMapping("/uploadImage")
    public ResponseEntity<Image> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            Image file = imageService.storeImageForBucketItem(image);

            //TODO: return Response?
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(file.getFileName())
                    .toUriString();

            //TODO: remove print
            System.out.println(fileDownloadUri);

            return ResponseEntity.accepted().body(file);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    @GetMapping("/images/{imageId:.+}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId, HttpServletRequest request) {
        try {

            // Load file as Resource
            Image imageFile = imageService.getImage(imageId);

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