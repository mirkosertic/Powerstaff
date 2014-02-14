package de.powerstaff.business.dao;

import de.mogwai.common.dao.DAO;
import de.powerstaff.business.entity.Tag;
import de.powerstaff.business.entity.TagType;

import java.util.List;

public interface TagDAO extends DAO {

    List<Tag> findTagSuggestion(String aSuggest, TagType aTagType);

    List<Tag> findTagsBy(TagType aTagType);
}
