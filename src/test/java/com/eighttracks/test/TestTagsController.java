package com.eighttracks.test;

import com.eighttracks.model.TagItem;
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
import java.util.List;

import static com.eighttracks.test.TestUtils.buildUrlEncodedFormEntity;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestTagsController {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddAndRemoveTag() throws Exception {
        String newTagName = "coldplay";
        addTag(newTagName);
        removeTag(newTagName);
    }

    @Test
    public void testGetAllTags() throws Exception {
//        List<String> addNewTags = Arrays.asList("coldplay", "linkinpark1", "imagineDragons1");
//        for (String addNewTag : addNewTags) {
//            addTag(addNewTag);
//        }
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/tags/list");
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<TagItem>>() {
        }.getType();
        List<TagItem> tagItemList = gson.fromJson(result.getResponse().getContentAsString(), listType);
        assertTrue("List count doesn't match", tagItemList.size() == 6);
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

    private void addTag(String tagName) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/tags/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(buildUrlEncodedFormEntity("tagName", tagName));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));
    }
}
