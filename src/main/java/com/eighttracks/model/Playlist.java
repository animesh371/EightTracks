package com.eighttracks.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Builder
@Getter
public class Playlist {
    private String playListName;
    private String notes;
    private Date createdAt;
    private Integer playCount;
    private Integer likeCount;
    private Integer trackCount;
    private List<String> songs;
}
