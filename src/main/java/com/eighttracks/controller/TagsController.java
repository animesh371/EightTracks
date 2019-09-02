package com.eighttracks.controller;

import com.eighttracks.model.TagItem;
import com.eighttracks.service.TagHandlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/tags")
public class TagsController {

    @Autowired
    TagHandlingService tagHandlingService;

    @PostMapping("/create")
    public void addTag(@RequestParam String tagName) {
        tagHandlingService.addTag(tagName);
    }

    @PostMapping("/delete")
    public void removeTag(@RequestParam String tagName) {
        tagHandlingService.removeTag(tagName);
    }

    @GetMapping("/list")
    public List<TagItem> list() {
        return tagHandlingService.list();
    }

    @GetMapping("/get")
    public TagItem getTagbyName(@RequestParam String tagName) {
        return tagHandlingService.getTagbyName(tagName);
    }
}
