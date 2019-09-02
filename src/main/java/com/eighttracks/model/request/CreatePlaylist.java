package com.eighttracks.model.request;

import com.eighttracks.model.Playlist;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class CreatePlaylist {
    private String playListName;
    private List<String> songs;
    private String notes;
    private Integer trackCount;

    public CreatePlaylist(String playListName, List<String> songs, String notes, Integer trackCount) {
        this.playListName = playListName;
        this.songs = songs;
        this.notes = notes;
        this.trackCount = trackCount;
    }

    public CreatePlaylist() {
    }
}
