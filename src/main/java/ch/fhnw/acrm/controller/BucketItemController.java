/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.acrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/item")
public class BucketItemController {

    @GetMapping
    public String getBucketItemListView(){
        return "bucket/itemList.html";
    }

    @GetMapping("/create")
    public String getBucketItemCreateView(){
        return "../bucket/itemCreate.html";
    }

    @GetMapping("/view")
    public String getBucketItemDetailView(){
        return "../bucket/itemView.html";
    }

    @GetMapping("/edit")
    public String getBucketItemEditView(){
        return "../bucket/itemEdit.html";
    }
}
