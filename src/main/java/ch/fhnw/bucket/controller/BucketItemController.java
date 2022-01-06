/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bucket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/item")
public class BucketItemController {

    @GetMapping
    public String getBucketItemListView(){
        return "/views/itemList.html";
    }

    @GetMapping("/create")
    public String getBucketItemCreateView(){
        return "/views/itemCreate.html";
    }

    @GetMapping("/view")
    public String getBucketItemDetailView(){
        return "/views/itemView.html";
    }

    @GetMapping("/edit")
    public String getBucketItemEditView(){
        return "/views/itemEdit.html";
    }
}
