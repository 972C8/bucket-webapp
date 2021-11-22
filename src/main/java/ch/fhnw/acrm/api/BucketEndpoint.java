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
        BucketItem bucketItem = null;
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

//    //TODO: Create api request to automatically use current user?
    //TODO: use /api/bucket-items?avatar-id=2
//    @GetMapping(path = "/bucket-items/{avatarId}", produces = "application/json")
//    public List<BucketItem> getBucketItemsByAvatar(@PathVariable(value = "avatarId") String avatarId) {
//        return bucketService.findAllBucketItemsByAvatar(Long.parseLong(avatarId));
//    }
}