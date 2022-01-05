package ch.fhnw.acrm.api;

import ch.fhnw.acrm.business.service.LabelService;
import ch.fhnw.acrm.data.domain.Label;
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

}