package ch.fhnw.bucket.api;

import ch.fhnw.bucket.business.service.BucketItemService;
import ch.fhnw.bucket.data.domain.BucketItem;
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
public class BucketItemEndpoint {
    @Autowired
    private BucketItemService bucketItemService;

    @PostMapping(path = "/bucket-items", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BucketItem> postBucketItem(@RequestBody BucketItem bucketItem) {
        try {
            bucketItem = bucketItemService.saveBucketItem(bucketItem);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{itemId}")
                .buildAndExpand(bucketItem.getId()).toUri();

        return ResponseEntity.created(location).body(bucketItem);
    }

    /*
    Get bucket item by id and current avatar
     */
    @GetMapping(path = "/bucket-items/{itemId}", produces = "application/json")
    public ResponseEntity<BucketItem> getBucketItem(@PathVariable(value = "itemId") String itemId) {
        BucketItem bucketItem;
        try {
            bucketItem = bucketItemService.findBucketItemByIdAndCurrentAvatar(Long.parseLong(itemId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(bucketItem);
    }

    @PutMapping(path = "/bucket-items/{itemId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BucketItem> putBucketItem(@RequestBody BucketItem bucketItem, @PathVariable(value = "itemId") String itemId) {
        try {
            bucketItem.setId(Long.parseLong(itemId));
            bucketItem = bucketItemService.saveBucketItem(bucketItem);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().body(bucketItem);
    }

    @DeleteMapping(path = "/bucket-items/{itemId}")
    public ResponseEntity<Void> deleteBucketItem(@PathVariable(value = "itemId") String itemId) {
        try {
            bucketItemService.deleteBucketItem(Long.parseLong(itemId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }

    /*
        Returns List<BucketItem> of bucket items assigned to the given avatar

        Boolean completed must be of Boolean (not primitive type boolean!) so that it can be null if no parameter was sent,
        else it is false as default
     */
    @GetMapping(path = "/bucket-items", produces = "application/json")
    public List<BucketItem> getBucketItems(
            @RequestParam(value = "bucketId", required = false) Long bucketId,
            @RequestParam(value = "completed", required = false) Boolean completed) {
        return bucketItemService.findAllBucketItems(bucketId, completed);
    }
}