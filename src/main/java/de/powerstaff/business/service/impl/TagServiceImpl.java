package de.powerstaff.business.service.impl;

import de.powerstaff.business.dao.TagDAO;
import de.powerstaff.business.entity.Tag;
import de.powerstaff.business.entity.TagType;
import de.powerstaff.business.service.TagService;

import java.util.List;

public class TagServiceImpl implements TagService {

    private TagDAO tagDAO;

    public void setTagDAO(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    @Override
    public List<Tag> findTagsSuggestion(String aSuggest, TagType aTagType) {
        return tagDAO.findTagSuggestion(aSuggest, aTagType);
    }

    @Override
    public List<Tag> findTagsBy(TagType aTagType) {
        return tagDAO.findTagsBy(aTagType);
    }

    @Override
    public Tag getTagByID(Long aTagID) {
        return (Tag) tagDAO.getById(Tag.class, aTagID);
    }
}
