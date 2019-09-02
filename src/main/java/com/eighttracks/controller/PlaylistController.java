package com.eighttracks.controller;

import com.eighttracks.dao.PlayListDao;
import com.eighttracks.model.Playlist;
import com.eighttracks.model.request.AddTags;
import com.eighttracks.model.request.CreatePlaylist;
import com.eighttracks.service.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    private PlayListService playListService;

    @GetMapping("/hello")
    public String index() {
        return "From Eight Tracks Controller";
    }

    @PostMapping("/create")
    public void createPlaylist(@RequestBody CreatePlaylist createPlaylist) {
        playListService.createPlaylist(createPlaylist);
    }

    @PostMapping("/remove")
    public void removePlaylist(@RequestParam Integer playlistId) {
        playListService.removePlaylist(playlistId);
    }

    @PostMapping("/update/likeCount")
    public void updateLike(@RequestParam Integer likeCount, @RequestParam Integer playlistId) {
        playListService.updateLike(likeCount, playlistId);
        System.out.println("Like count = " + likeCount + " playlist = "+ playlistId);
    }

    @PostMapping("/update/playCount")
    public void updatePlayCount(@RequestParam Integer playCount, @RequestParam Integer playlistId) {
        playListService.updatePlayCount(playCount, playlistId);
    }

    @GetMapping("/list")
    public List<Playlist> getAllPlayList() {
        return playListService.getAllPlayList();
    }

    @PostMapping("/addTags")
    public void addTags(AddTags addTags) {

    }
}
