/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bucket.controller;

import ch.fhnw.bucket.business.service.AvatarService;
import ch.fhnw.bucket.data.domain.Avatar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class UserController {

    @Autowired
    private AvatarService avatarService;

    @GetMapping("/login")
    public String getLoginView() {
        return "user/login.html";
    }

    @GetMapping("/user/register")
    public String getRegisterView() {
        return "register.html";
    }

    @PostMapping("/user/register")
    public ResponseEntity<Void> postRegister(@RequestBody Avatar avatar) {
        try {
            avatarService.saveAvatar(avatar);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile/edit")
    public String getProfileView() {
        return "../user/profile.html";
    }

    @GetMapping("/profile")
    public @ResponseBody
    Avatar getProfile() {
        return avatarService.getCurrentAvatar();
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> putProfile(@RequestBody Avatar avatar) {
        try {
            avatar.setId(avatarService.getCurrentAvatar().getId());
            avatarService.saveAvatar(avatar);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/validate", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ResponseEntity<Void> init() {
        return ResponseEntity.ok().build();
    }
}