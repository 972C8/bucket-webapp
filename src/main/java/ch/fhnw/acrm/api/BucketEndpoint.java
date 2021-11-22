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
}