package ch.fhnw.bucket.business.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

import ch.fhnw.bucket.data.domain.image.BucketItemImage;
import ch.fhnw.bucket.data.repository.BucketItemImageRepository;
import ch.fhnw.bucket.data.repository.ProfilePictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    @Autowired
    private BucketItemImageRepository bucketItemImageRepository;
    @Autowired
    private ProfilePictureRepository profilePictureRepository;

    private final Path root = Paths.get("uploads");

    /**
     * Create root directory if not exists yet
     */
    public void rootExists() {
        try {
            if (Files.notExists(root)) {
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    /**
     * Creates a randomized name for the image name
     *
     * @param name
     * @return
     */
    public String createRandomName(String name) {
        //generate the random string
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        String[] split = name.split("[.]");
        String suffix = split[split.length - 1];

        //Return the generated string and append the image type as suffix.
        //e.g. 1283239.png
        return generatedString + "." + suffix;
    }

    public BucketItemImage saveBucketItemImage(MultipartFile file) {
        try {
            //Check if root already exists
            rootExists();

            //Create a random image name
            String imageName = createRandomName(Objects.requireNonNull(file.getOriginalFilename()));

            //Upload image to root folder
            Path url = this.root.resolve(imageName);
            Files.copy(file.getInputStream(), url);

            //store the image information in the database
            BucketItemImage image = new BucketItemImage(imageName, url.toString(), file.getContentType());
            return bucketItemImageRepository.save(image);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    /**
     * Load BucketItemImage from bucketItemId
     *
     * @param bucketItemId
     * @return
     */
    public BucketItemImage loadBucketItemImage(Long bucketItemId) {
        try {
            return bucketItemImageRepository.findBucketItemImageByBucketItemId(bucketItemId);

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    /**
     * Load the resource using the bucket item image. Resource is used to provide image through API.
     *
     * @param item
     * @return
     */
    public Resource loadResourceFromBucketItemImage(BucketItemImage item) {
        try {
            //Find the image of the BucketItem
            Path file = root.resolve(item.getFileName());

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}