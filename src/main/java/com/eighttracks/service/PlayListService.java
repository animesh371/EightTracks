package com.eighttracks.service;

import com.eighttracks.dao.PlayListDao;
import com.eighttracks.model.Playlist;
import com.eighttracks.model.request.CreatePlaylist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayListService {

    @Autowired
    PlayListDao playListDao;

    public void createPlaylist(CreatePlaylist createPlaylist) {
        playListDao.createPlaylist(createPlaylist);
    }

    public void removePlaylist(Integer playlistId) {
        playListDao.removePlaylist(playlistId);
    }

    public void updateLike(Integer likeCount, Integer playlistId) {
        playListDao.updateLike(likeCount, playlistId);
    }


    public void updatePlayCount(Integer playCount, Integer playlistId) {
        playListDao.updatePlayCount(playCount, playlistId);
    }

    public List<Playlist> getAllPlayList() {
        return playListDao.getAllPlayList();
    }
}
