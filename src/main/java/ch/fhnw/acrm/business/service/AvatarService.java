/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.acrm.business.service;

import ch.fhnw.acrm.data.domain.Avatar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ch.fhnw.acrm.data.repository.AvatarRepository;

import javax.validation.Valid;
import javax.validation.Validator;

@Service
@Validated
public class AvatarService {

    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    Validator validator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveAvatar(@Valid Avatar avatar) throws Exception {
        if (avatar.getId() == null) {
            if (avatarRepository.findByEmail(avatar.getEmail()) != null) {
                throw new Exception("Email address " + avatar.getEmail() + " already assigned another avatar.");
            }
        } else if (avatarRepository.findByEmailAndIdNot(avatar.getEmail(), avatar.getId()) != null) {
            throw new Exception("Email address " + avatar.getEmail() + " already assigned another avatar.");
        }
        avatar.setPassword(passwordEncoder.encode(avatar.getPassword()));
        avatarRepository.save(avatar);
    }

    public Avatar getCurrentAvatar() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return avatarRepository.findByEmail(user.getUsername());
    }

    public Avatar findAvatarById(Long avatarId) throws Exception {
        Avatar avatar = avatarRepository.findAvatarById(avatarId);
        if (avatar == null) {
            throw new Exception("No customer with ID " + avatarId + " found.");
        }
        return avatar;
    }
}
