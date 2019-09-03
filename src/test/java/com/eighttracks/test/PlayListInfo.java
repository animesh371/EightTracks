package com.eighttracks.test;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PlayListInfo {
    String playlistName;
    List<String> songs;
    Integer trackCount;
    List<String> tags;
    int likeCount;
    int playedCount;
}
