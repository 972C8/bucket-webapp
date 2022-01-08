package ch.fhnw.bucket.api;

import ch.fhnw.bucket.business.service.LabelService;
import ch.fhnw.bucket.data.domain.Label;
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
public class LabelEndpoint {
    @Autowired
    private LabelService labelService;

    @PostMapping(path = "/labels", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Label> postLabel(@RequestBody Label label) {
        try {
            label = labelService.saveLabel(label);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{labelId}")
                .buildAndExpand(label.getId()).toUri();

        return ResponseEntity.created(location).body(label);
    }

    /*
    Get label by id and current avatar
     */
    @GetMapping(path = "/labels/{labelId}", produces = "application/json")
    public ResponseEntity<Label> getLabel(@PathVariable(value = "labelId") String labelId) {
        Label label;
        try {
            label = labelService.findLabelByIdAndCurrentAvatar(Long.parseLong(labelId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(label);
    }

    @PutMapping(path = "/labels/{labelId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Label> putLabel(@RequestBody Label label, @PathVariable(value = "labelId") String labelId) {
        try {
            label.setId(Long.parseLong(labelId));
            label = labelService.updateLabel(label);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().body(label);
    }


    @DeleteMapping(path = "/labels/{labelId}")
    public ResponseEntity<Void> deleteLabel(@PathVariable(value = "labelId") String labelId) {
        try {
            labelService.deleteLabel(Long.parseLong(labelId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }

    /*
    Returns List<Bucket> of buckets assigned to the given avatar
    */
    @GetMapping(path = "/labels", produces = "application/json")
    public List<Label> geLabelItems() {
        return labelService.findAllLabels();
    }
}