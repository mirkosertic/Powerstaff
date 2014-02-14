package de.powerstaff.web.backingbean.tags;

import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.Tag;
import de.powerstaff.business.entity.TagType;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.TagService;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

public class TagsBackingBean extends WrappingBackingBean<TagsBackingBeanDataModel> implements StateHolder {

    private TagService tagService;

    private FreelancerService freelancerService;

    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    public void setFreelancerService(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    public List<Tag> getTagsSchwerpunkte() {
        return tagService.findTagsBy(TagType.SCHWERPUNKT);
    }

    public List<Tag> getTagsFunktionen() {
        return tagService.findTagsBy(TagType.FUNKTION);
    }

    public List<Tag> getTagsEinsatzorte() {
        return tagService.findTagsBy(TagType.EINSATZORT);
    }

    public List<Tag> getTagsBemerkungen() {
        return tagService.findTagsBy(TagType.BEMERKUNG);
    }

    @Override
    protected TagsBackingBeanDataModel createDataModel() {
        return new TagsBackingBeanDataModel();
    }

    public void loadEntity() {
        Tag theTag = tagService.getTagByID(getData().getCurrentTagId());
        getData().setTag(theTag);

        List<Freelancer> theFreelancer = freelancerService.findFreelancerByTag(theTag);
        getData().initFreelancerList(theFreelancer);
    }

    public void restoreState(FacesContext aContext, Object aValue) {
        Object[] theData = (Object[]) aValue;
        setData((TagsBackingBeanDataModel) theData[0]);
    }

    public Object saveState(FacesContext aContext) {
        ArrayList theData = new ArrayList();
        theData.add(getData());
        return theData.toArray();
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public void setTransient(boolean b) {
    }
}
