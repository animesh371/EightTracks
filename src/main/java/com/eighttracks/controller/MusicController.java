package com.eighttracks.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/music")
public class MusicController {

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public void uploadMusicFile(@RequestParam("file") MultipartFile file) {
        System.out.println(file.getContentType() + " " +
        file.getOriginalFilename());
    }

}
