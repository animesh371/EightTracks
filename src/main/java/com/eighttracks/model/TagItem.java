package com.eighttracks.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TagItem {
    private String tagName;
    private String tagId;
}
