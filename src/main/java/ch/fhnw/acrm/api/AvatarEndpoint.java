package ch.fhnw.acrm.api;

import ch.fhnw.acrm.business.service.AvatarService;
import ch.fhnw.acrm.data.domain.Avatar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api")
public class AvatarEndpoint {
    @Autowired
    private AvatarService avatarService;

    @GetMapping(path = "/avatars/{avatarId}", produces = "application/json")
    public ResponseEntity<Avatar> getAvatar(@PathVariable(value = "avatarId") String avatarId) {
        Avatar avatar = null;
        try {
            avatar = avatarService.findAvatarById(Long.parseLong(avatarId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(avatar);
    }
}