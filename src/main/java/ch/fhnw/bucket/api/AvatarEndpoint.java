package ch.fhnw.bucket.api;

import ch.fhnw.bucket.business.service.AvatarService;
import ch.fhnw.bucket.data.domain.Avatar;
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
            avatar = avatarService.saveAvatar(avatar);
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

    @PutMapping(path = "/avatars/{avatarId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Avatar> putAvatar(@RequestBody Avatar avatar, @PathVariable(value = "avatarId") String avatarId) {
        try {
            avatar.setId(Long.parseLong(avatarId));
            avatar = avatarService.saveAvatar(avatar);
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().body(avatar);
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

    @DeleteMapping(path = "/avatars/{avatarId}")
    public ResponseEntity<Void> deleteAvatar(@PathVariable(value = "avatarId") String avatarId) {
        try {
            avatarService.deleteAvatar(Long.parseLong(avatarId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }
}