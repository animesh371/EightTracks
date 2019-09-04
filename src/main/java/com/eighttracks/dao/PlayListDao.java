package com.eighttracks.dao;

import com.eighttracks.components.PostgresModule;
import com.eighttracks.model.Playlist;
import com.eighttracks.model.TagItem;
import com.eighttracks.model.request.AddTags;
import com.eighttracks.model.request.CreatePlaylist;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class PlayListDao {

    private static final String PLAY_COUNT = "playCount";
    private static final String TRACK_COUNT = "trackCount";
    private static final String LIKE_COUNT = "likeCount";
    private static final String PLAY_LIST_NAME = "playListName";
    private static final String NOTES = "notes";
    public static final String SONGS = "songs";
    private static final String INSERT_INTO_PLAYLIST = "INSERT INTO playlists (playlistname, trackcount, songs, createdat) values (?, ? , to_json(?::jsonb) ,?)";
    private static final String DELETE_FROM_PLAYLIST = "Delete from playlists where playlistId = ? ";
    private static final String UPDATE_LIKE_PLAYLIST = "Update playlists set likeCount = ? where playlistId = ?";
    private static final String UPDATE_PLAYCOUNT_PLAYLIST = "Update playlists set likeCount = ? where playlistId = ?";
    private static final String GET_ALL_PLAYLISTS = "Select * from playlists";
    public static final String CREATED_AT = "createdAt";
    public static final String PLAYLIST_ID = "playlistId";
    @Autowired
    private PostgresModule postgressModule;
    private final Gson gson = new Gson();

    public void createPlaylist(CreatePlaylist createPlaylist) {
        postgressModule.getDb()
                .update(INSERT_INTO_PLAYLIST,
                        createPlaylist.getPlayListName(), createPlaylist.getTrackCount(),
                        gson.toJson(createPlaylist.getSongs()), new Date());

    }

    public void removePlaylist(Integer playlistId) {
        postgressModule.getDb()
                .update(DELETE_FROM_PLAYLIST, playlistId);
    }

    public void updateLike(Integer likeCount, Integer playlistId) {
        postgressModule.getDb()
                .update(UPDATE_LIKE_PLAYLIST,
                        likeCount, playlistId);
    }


    public void updatePlayCount(Integer playCount, Integer playlistId) {
        postgressModule.getDb()
                .update(UPDATE_PLAYCOUNT_PLAYLIST,
                        playCount, playlistId);
    }

    public List<Playlist> getAllPlayList() {
        return postgressModule.getDb()
                .query(GET_ALL_PLAYLISTS, ((resultSet, i) -> playlistItemMapper(resultSet)));
    }

    public void addTags(AddTags addTags) {
        for (Integer tag : addTags.getTags()) {
            postgressModule.getDb()
                    .update("INSERT INTO playlistTagMapping (playlistId, tagId) values (?,?)",
                            addTags.getPlaylistId(), tag);
        }
    }

    public List<Playlist> getPlayListFromTags(List<String> tagList) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("tags", tagList);

        return postgressModule.getNamedJDBCTemplate()
                .query("select p.playlistid, p.playlistname, p.playcount, p.likecount, p.trackcount, p.songs, count(p.playlistid) from " +
                "playlists p, tags t, playlisttagmapping ptm where t.tagid=ptm.tagid AND t.tagname IN (:tags) AND p.playlistid=ptm.playlistid group by p.playlistid order by count(p.playlistid) desc , likecount desc, trackcount desc", parameters, ((resultSet, i) -> playlistItemMapper(resultSet)) );
    }

    private Playlist playlistItemMapper(ResultSet resultSet) throws SQLException {
        Type listType = new TypeToken<ArrayList<TagItem>>() {
        }.getType();
        List<String> songs = null;
        try {
            songs = new ObjectMapper().readValue(resultSet.getString("songs"), List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Playlist.builder()
                .playlistId(Integer.parseInt(resultSet.getString(PLAYLIST_ID)))
                .playCount(Integer.parseInt(resultSet.getString(PLAY_COUNT)))
                .likeCount(Integer.parseInt(resultSet.getString(LIKE_COUNT)))
                //.notes(resultSet.getString(NOTES))
                .playListName(resultSet.getString(PLAY_LIST_NAME))
                .songs(songs)
                .trackCount(Integer.parseInt(resultSet.getString(TRACK_COUNT)))
                .build();
    }


}
