package ch.fhnw.acrm.business.service;

import ch.fhnw.acrm.data.domain.Image;
import ch.fhnw.acrm.data.repository.ImageRepository;
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

    /*
    Save image in database as byte
     */
    public Image storeImage(MultipartFile img) throws Exception {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(img.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Image image = new Image(fileName, img.getContentType(), img.getBytes());

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
