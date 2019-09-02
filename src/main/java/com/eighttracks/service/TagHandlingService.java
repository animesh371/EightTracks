package com.eighttracks.service;

import com.eighttracks.dao.TagDao;
import com.eighttracks.model.TagItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagHandlingService {

    @Autowired
    private TagDao tagDao;

    public void addTag(String tagName) {
        tagDao.addTag(tagName);
    }

    public void removeTag(String tagName) {
        tagDao.removeTag(tagName);
    }

    public List<TagItem> list() {
        return tagDao.getAllTags();
    }

    public TagItem getTagbyName(String tagName) {
        return tagDao.getTagByName(tagName);
    }
}
