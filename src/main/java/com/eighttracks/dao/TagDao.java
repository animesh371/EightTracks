package com.eighttracks.dao;

import com.eighttracks.components.PostgresModule;
import com.eighttracks.model.TagItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TagDao {
    private static final String INSERT_INTO_TAGS_QUERY = "INSERT INTO tags (tagName) values (?) ";
    private static final String DELETE_FROM_TAGS_QUERY = "DELETE from tags where tagName = ? ";
    private static final String GET_ALL_TAGS = "Select * from tags";
    private static final String TAG_ID = "tagId";
    private static final String TAG_NAME = "tagName";

    @Autowired
    private PostgresModule postgressModule;

    public void addTag(String tagName) {
        postgressModule.getDb()
                .update(INSERT_INTO_TAGS_QUERY, tagName);
    }

    public void removeTag(String tagName) {
        postgressModule.getDb()
                .update(DELETE_FROM_TAGS_QUERY, tagName);
    }

    public List<TagItem> getAllTags() {
        return postgressModule.getDb()
                .query(GET_ALL_TAGS, ((resultSet, i) -> tagItemMapper(resultSet)));
    }

    public TagItem getTagByName(String tagName) {
        return postgressModule.getDb()
                .queryForObject("Select * from tags where tagName = ? ",(resultSet, i) -> tagItemMapper(resultSet), tagName);
    }

    private TagItem tagItemMapper(ResultSet resultSet) throws SQLException {
        return TagItem.builder()
                .tagId(resultSet.getString(TAG_ID))
                .tagName(resultSet.getString(TAG_NAME))
                .build();
    }
}
