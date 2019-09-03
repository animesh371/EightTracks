package com.eighttracks.test;

import com.eighttracks.model.Playlist;
import com.eighttracks.model.TagItem;
import com.eighttracks.model.request.AddTags;
import com.eighttracks.model.request.CreatePlaylist;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Type;
import java.util.*;

import static com.eighttracks.test.TestUtils.buildUrlEncodedFormEntity;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestPlaylistController {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreatePlaylist() throws Exception {
        CreatePlaylist createPlaylist = getPlayList("HeavyMetal", Arrays.asList("song1", "song2", "song3"), 3);
        MvcResult result = createPlayList(createPlaylist);

        System.out.println(result.getResponse().getStatus());
    }

    private MvcResult createPlayList(CreatePlaylist createPlaylist) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/playlist/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPlaylist));

        return mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
    }

    private MvcResult addTag(AddTags addTags) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/playlist/addTags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addTags));

        return mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    public void testGetAllPlaylist() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/playlist/list");
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Playlist>>() {
        }.getType();
        List<Playlist> playlists = gson.fromJson(result.getResponse().getContentAsString(), listType);
        for (Playlist playlist : playlists) {
            System.out.println(playlist.getCreatedAt() + " " + playlist.getSongs());
        }
    }

    List<TagItem> getTagItems() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/tags/list");
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<TagItem>>() {
        }.getType();
        List<TagItem> tagItemList = gson.fromJson(result.getResponse().getContentAsString(), listType);
        return tagItemList;
    }

    List<Playlist> getPlayList() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/playlist/list");
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Playlist>>() {
        }.getType();
        List<Playlist> playlists = gson.fromJson(result.getResponse().getContentAsString(), listType);

        return playlists;
    }

    private CreatePlaylist getPlayList(String playlistName, List<String> songs, Integer trackCount) {
        return new CreatePlaylist(playlistName, songs,
                "", trackCount);
    }

    @Test
    public void testHello() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/playlist/hello");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse().getStatus());
    }

    @Test
    public void testSearchByTags() throws Exception {
        List<String> tagList = Arrays.asList("tag1", "tag2", "tag3", "tag4", "tag5");
        for(String tag : tagList) {
            addTag(tag);
        }

        Map<String, PlayListInfo> playListInfoMap = getPlayListInfoMap();
        for (Map.Entry<String, PlayListInfo> entry : playListInfoMap.entrySet()) {
            CreatePlaylist createPlaylist = getPlayList(entry.getValue().getPlaylistName(), entry.getValue().getSongs(), entry.getValue().getTrackCount());
            MvcResult result = createPlayList(createPlaylist);
        }
        List<TagItem> tagItems = getTagItems();
        Map<String, Integer> tagItemMap = new HashMap<>();
        for(TagItem tagItem: tagItems) {
            tagItemMap.put(tagItem.getTagName(), Integer.parseInt(tagItem.getTagId()));
        }

        List<Playlist> playlists = getPlayList();
        for (Playlist playlist : playlists) {
            PlayListInfo playListInfo = playListInfoMap.get(playlist.getPlayListName());
            List<Integer> tagIds = new ArrayList<>();
            for (String tagName: playListInfo.getTags()) {
                tagIds.add(tagItemMap.get(tagName));
            }
            AddTags addTags = new AddTags(playlist.getPlaylistId(), tagIds);
            addTag(addTags);
        }

//        for(String tag : tagList) {
//            removeTag(tag);
//        }
        List<Playlist> playlists1 = searchForPlaylistsByTags("tag1+tag3");
        System.out.println(new Gson().toJsonTree(playlists1));

    }

    private List<Playlist> searchForPlaylistsByTags(String query) throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/playlist/seachByTags?query="+ query);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Playlist>>() {
        }.getType();
        List<Playlist> playlists = gson.fromJson(result.getResponse().getContentAsString(), listType);

        return playlists;

    }

    private void addTag(String tagName) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/tags/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(buildUrlEncodedFormEntity("tagName", tagName));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));
    }

    private void removeTag(String tagName) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/tags/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(buildUrlEncodedFormEntity("tagName", tagName));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));
    }

    private Map<String, PlayListInfo> getPlayListInfoMap() {
        Map<String, PlayListInfo> playListInfoMap = new HashMap<>();
        playListInfoMap.put("playlist1", getPlayList("playlist1", 10, Arrays.asList("tag1", "tag2", "tag3"), 100, 20));
        playListInfoMap.put("playlist2", getPlayList("playlist2", 20, Arrays.asList("tag1", "tag2", "tag3"), 799, 80));
        playListInfoMap.put("playlist3", getPlayList("playlist3", 30, Arrays.asList("tag1", "tag2", "tag3"), 120, 40));
        playListInfoMap.put("playlist4", getPlayList("playlist4", 40, Arrays.asList("tag5", "tag4"), 100, 20));
        playListInfoMap.put("playlist5", getPlayList("playlist5", 50, Arrays.asList("tag1"), 100, 60));
        return playListInfoMap;
    }

    private PlayListInfo getPlayList(String playlistName, Integer trackCount,
                                     List<String> tags,
                                     int likeCount,
                                     int playedCount) {
        return PlayListInfo
                .builder()
                .trackCount(trackCount)
                .playlistName(playlistName)
                .likeCount(likeCount)
                .playedCount(playedCount)
                .songs(Arrays.asList("song1", "song2", "song3"))
                .tags(tags)
                .build();
    }
}
