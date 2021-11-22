package ch.fhnw.acrm.api;

import ch.fhnw.acrm.business.service.BucketService;
import ch.fhnw.acrm.data.domain.BucketItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class BucketEndpoint {
    @Autowired
    private BucketService bucketService;

    @PostMapping(path = "/bucket-items", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BucketItem> postBucketItem(@RequestBody BucketItem bucketItem) {
        try {
            bucketItem = bucketService.editBucketItem(bucketItem);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{ItemId}")
                .buildAndExpand(bucketItem.getId()).toUri();

        return ResponseEntity.created(location).body(bucketItem);
    }

    @GetMapping(path = "/bucket-items/{itemId}", produces = "application/json")
    public ResponseEntity<BucketItem> getBucketItem(@PathVariable(value = "itemId") String itemId) {
        BucketItem bucketItem;
        try {
            bucketItem = bucketService.findBucketItemById(Long.parseLong(itemId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(bucketItem);
    }

    @PutMapping(path = "/bucket-items/{itemId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BucketItem> putCustomer(@RequestBody BucketItem bucketItem, @PathVariable(value = "itemId") String itemId) {
        try {
            bucketItem.setId(Long.parseLong(itemId));
            bucketItem = bucketService.editBucketItem(bucketItem);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().body(bucketItem);
    }

    @DeleteMapping(path = "/bucket-items/{itemId}")
    public ResponseEntity<Void> deleteBucketItem(@PathVariable(value = "itemId") String itemId) {
        try {
            bucketService.deleteBucketItem(Long.parseLong(itemId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }

    /*
        Returns List<BucketItem> of bucket items assigned to the given avatar
        Requests must be made in the format of "/api/bucket-items?avatar-id=123"
     */
    @GetMapping(path = "/bucket-items", produces = "application/json")
    public List<BucketItem> getBucketItemsByAvatar(@RequestParam(value = "avatar-id") String avatarId) {
        return bucketService.findAllBucketItemsByAvatar(Long.parseLong(avatarId));
    }
}