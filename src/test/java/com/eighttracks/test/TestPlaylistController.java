package com.eighttracks.test;

import com.eighttracks.model.Playlist;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestPlaylistController {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreatePlaylist() throws Exception {
        CreatePlaylist createPlaylist = getPlayList();
        ObjectMapper objectMapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/playlist/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPlaylist));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getStatus());
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
        for (Playlist playlist: playlists) {
            System.out.println(playlist.getCreatedAt() + " " + playlist.getSongs());
        }
    }

    private CreatePlaylist getPlayList() {
        return new CreatePlaylist("HeavyMetal", Arrays.asList("song1", "song2", "song3"),
                "", 3);
    }

    @Test
    public void testHello() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/playlist/hello");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse().getStatus());
    }
}
