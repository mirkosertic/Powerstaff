package de.powerstaff.business.service;

import de.powerstaff.business.entity.Tag;
import de.powerstaff.business.entity.TagType;

import java.util.List;

public interface TagService {

    List<Tag> findTagsSuggestion(String aSuggest, TagType aTagType);

    Tag getTagByID(Long aTagID);
}
