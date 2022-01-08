/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bucket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/bucket")
public class BucketController {

    @GetMapping
    public String getBucketListView(){
        return "/views/bucketList.html";
    }

    @GetMapping("/create")
    public String getBucketCreateView(){
        return "/views/bucketCreate.html";
    }

    @GetMapping("/view")
    public String getBucketDetailView(){
        return "/views/bucketView.html";
    }

    @GetMapping("/edit")
    public String getBucketEditView(){
        return "/views/bucketEdit.html";
    }
}
