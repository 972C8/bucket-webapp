/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

 package ch.fhnw.bucket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/dashboard")
public class DashboardController {

    @GetMapping
    public String getDashboardView(){
        return "/views/dashboard.html";
    }
}
