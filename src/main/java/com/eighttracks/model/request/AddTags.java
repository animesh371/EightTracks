package com.eighttracks.model.request;


import lombok.Getter;

import java.util.List;

@Getter
public class AddTags {
    private Integer playlistId;
    private List<Integer> tags;

    public AddTags(Integer playlistId, List<Integer> tags) {
        this.playlistId = playlistId;
        this.tags = tags;
    }

    public AddTags() {
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public List<Integer> getTags() {
        return tags;
    }
}
