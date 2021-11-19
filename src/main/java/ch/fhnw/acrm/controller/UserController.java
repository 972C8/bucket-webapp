/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.acrm.controller;

import ch.fhnw.acrm.business.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ch.fhnw.acrm.data.domain.Agent;

@Controller
public class UserController {

    @Autowired
    private AgentService agentService;

    @GetMapping("/login")
    public String getLoginView() {
        return "user/login.html";
    }

    @GetMapping("/user/register")
    public String getRegisterView() {
        return "register.html";
    }

    @PostMapping("/user/register")
    public ResponseEntity<Void> postRegister(@RequestBody Agent agent) {
        try {
            agentService.saveAgent(agent);
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
    public @ResponseBody Agent getProfile() {
        return agentService.getCurrentAgent();
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> putProfile(@RequestBody Agent agent) {
        try {
            agent.setId(agentService.getCurrentAgent().getId());
            agentService.saveAgent(agent);
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