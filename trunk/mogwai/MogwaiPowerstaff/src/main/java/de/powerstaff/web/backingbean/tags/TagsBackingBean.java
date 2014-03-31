package de.powerstaff.web.backingbean.tags;

import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.Tag;
import de.powerstaff.business.entity.TagType;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.TagService;
import de.powerstaff.web.backingbean.MessageConstants;
import org.apache.commons.lang.StringUtils;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagsBackingBean extends WrappingBackingBean<TagsBackingBeanDataModel> implements StateHolder, MessageConstants {

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

    private Set<Long> computeTagIDs() {
        Set<Long> theTagsIDs = new HashSet();
        for (String theID : StringUtils.split(getData().getCurrentTagId(), ',')) {
            theTagsIDs.add(Long.valueOf(theID));
        }
        return theTagsIDs;
    }

    public void load() {
        loadAndSortByName1();
    }

    public void loadAndSortByName1() {
        getData().initFreelancerList(freelancerService.findFreelancerByTagIDsSortByName1(computeTagIDs()));
    }

    public void loadAndSortByName2() {
        getData().initFreelancerList(freelancerService.findFreelancerByTagIDsSortByName2(computeTagIDs()));
    }

    public void loadAndSortByCode() {
        getData().initFreelancerList(freelancerService.findFreelancerByTagIDsSortByCode(computeTagIDs()));
    }

    public void loadAndSortByAvailability() {
        getData().initFreelancerList(freelancerService.findFreelancerByTagIDsSortByAvailability(computeTagIDs()));
    }

    public void loadAndSortBySallary() {
        getData().initFreelancerList(freelancerService.findFreelancerByTagIDsSortBySallary(computeTagIDs()));
    }

    public void loadAndSortByPlz() {
        getData().initFreelancerList(freelancerService.findFreelancerByTagIDsSortByPlz(computeTagIDs()));
    }

    public void loadAndSortByLastContact() {
        getData().initFreelancerList(freelancerService.findFreelancerByTagIDsSortByLastContact(computeTagIDs()));
    }

    public String commandSearch() throws IOException {

        FacesContext theContext = FacesContext.getCurrentInstance();
        HttpServletRequest theRequest = (HttpServletRequest) theContext.getExternalContext().getRequest();

        StringBuilder theSearchTags = new StringBuilder();
        String[] theSelectedTags = theRequest.getParameterValues("selectedtag");
        if (theSelectedTags != null) {
            for (String theValue : theSelectedTags) {
                if (theSearchTags.length() > 0) {
                    theSearchTags.append(",");
                }
                theSearchTags.append(theValue);
            }

            getData().setCurrentTagId(theSearchTags.toString());

            return "pretty:tagsearch";

        }

        JSFMessageUtils.addGlobalErrorMessage(MSG_KEINETAGSAUSGEWAEHLT);
        return null;
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
