package de.powerstaff.web.backingbean.tags;

import de.mogwai.common.web.backingbean.BackingBeanDataModel;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagsBackingBeanDataModel extends BackingBeanDataModel {

    private long currentTagId;
    private CollectionDataModel<Freelancer> freelancer;
    private Tag tag;

    public TagsBackingBeanDataModel() {
        freelancer = new CollectionDataModel<Freelancer>(new ArrayList<Freelancer>());
    }

    public long getCurrentTagId() {
        return currentTagId;
    }

    public void setCurrentTagId(long currentTagId) {
        this.currentTagId = currentTagId;
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
}
