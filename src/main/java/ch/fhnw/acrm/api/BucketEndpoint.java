package ch.fhnw.acrm.api;

import ch.fhnw.acrm.business.service.BucketService;
import ch.fhnw.acrm.data.domain.Bucket;
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

    @PostMapping(path = "/buckets", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Bucket> postBucket(@RequestBody Bucket bucket) {
        try {
            bucket = bucketService.saveBucket(bucket);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{bucketId}")
                .buildAndExpand(bucket.getId()).toUri();

        return ResponseEntity.created(location).body(bucket);
    }

    @GetMapping(path = "/buckets/{bucketId}", produces = "application/json")
    public ResponseEntity<Bucket> getBucket(@PathVariable(value = "bucketId") String bucketId) {
        Bucket bucket;
        try {
            bucket = bucketService.findBucketById(Long.parseLong(bucketId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(bucket);
    }

    @PutMapping(path = "/buckets/{bucketId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Bucket> putBucket(@RequestBody Bucket bucket, @PathVariable(value = "bucketId") String bucketId) {
        try {
            bucket.setId(Long.parseLong(bucketId));
            bucket = bucketService.updateBucket(bucket);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().body(bucket);
    }

    @DeleteMapping(path = "/buckets/{bucketId}")
    public ResponseEntity<Void> deleteBucket(@PathVariable(value = "bucketId") String bucketId) {
        try {
            bucketService.deleteBucket(Long.parseLong(bucketId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }

    /*
        Returns List<Bucket> of buckets assigned to the given avatar
     */
    @GetMapping(path = "/buckets", produces = "application/json")
    public List<Bucket> getBucketItems() {
        return bucketService.findAllBuckets();
    }
}