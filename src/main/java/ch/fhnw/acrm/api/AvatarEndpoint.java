package ch.fhnw.acrm.api;

import ch.fhnw.acrm.business.service.AvatarService;
import ch.fhnw.acrm.data.domain.Avatar;
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
public class AvatarEndpoint {
    @Autowired
    private AvatarService avatarService;

    @PostMapping(path = "/avatars", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Avatar> postAvatar(@RequestBody Avatar avatar) {
        try {
            avatar = avatarService.editAvatar(avatar);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{avatarId}")
                .buildAndExpand(avatar.getId()).toUri();

        return ResponseEntity.created(location).body(avatar);
    }

    @GetMapping(path = "/avatars/{avatarId}", produces = "application/json")
    public ResponseEntity<Avatar> getAvatar(@PathVariable(value = "avatarId") String avatarId) {
        Avatar avatar;
        try {
            avatar = avatarService.findAvatarById(Long.parseLong(avatarId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(avatar);
    }
}