package de.powerstaff.web.backingbean.tags;

import de.mogwai.common.web.backingbean.BackingBeanDataModel;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.Tag;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class TagsBackingBeanDataModel extends BackingBeanDataModel {

    private String currentTagId;
    private CollectionDataModel<Freelancer> freelancer;
    private Tag tag;

    public TagsBackingBeanDataModel() {
        freelancer = new CollectionDataModel<Freelancer>(new ArrayList<Freelancer>());
    }

    public CollectionDataModel<Freelancer> getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(CollectionDataModel<Freelancer> freelancer) {
        this.freelancer = freelancer;
    }

    public void initFreelancerList(List<Freelancer> aFreelancer) {
        freelancer.setWrappedData(aFreelancer);
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getCurrentTagId() {
        return currentTagId;
    }

    public void setCurrentTagId(String currentTagId) {
        HttpServletRequest theRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            this.currentTagId = URLDecoder.decode(currentTagId, theRequest.getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
