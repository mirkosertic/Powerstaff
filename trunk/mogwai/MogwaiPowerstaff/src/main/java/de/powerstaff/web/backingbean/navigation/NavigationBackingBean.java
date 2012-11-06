package de.powerstaff.web.backingbean.navigation;

import de.mogwai.common.web.backingbean.BackingBean;
import de.powerstaff.web.backingbean.customer.CustomerBackingBean;
import de.powerstaff.web.backingbean.freelancer.FreelancerBackingBean;
import de.powerstaff.web.backingbean.partner.PartnerBackingBean;
import de.powerstaff.web.backingbean.profile.ProfileBackingBean;
import de.powerstaff.web.backingbean.project.ProjectBackingBean;

public class NavigationBackingBean extends BackingBean {

    private static final long serialVersionUID = -2068461554004949119L;

    private FreelancerBackingBean freelancerBackingBean;
    private PartnerBackingBean partnerBackingBean;
    private ProjectBackingBean projectBackingBean;
    private CustomerBackingBean customerBackingBean;
    private ProfileBackingBean profileBackingBean;

    public void setFreelancerBackingBean(FreelancerBackingBean freelancerBackingBean) {
        this.freelancerBackingBean = freelancerBackingBean;
    }

    public void setPartnerBackingBean(PartnerBackingBean partnerBackingBean) {
        this.partnerBackingBean = partnerBackingBean;
    }

    public void setProjectBackingBean(ProjectBackingBean projectBackingBean) {
        this.projectBackingBean = projectBackingBean;
    }

    public void setCustomerBackingBean(CustomerBackingBean customerBackingBean) {
        this.customerBackingBean = customerBackingBean;
    }

    public void setProfileBackingBean(ProfileBackingBean profileBackingBean) {
        this.profileBackingBean = profileBackingBean;
    }

    public String commandFreelancer() {
        freelancerBackingBean.reload();
        return "pretty:freelancermain";
    }

    public String commandPartner() {
        partnerBackingBean.reload();
        return "pretty:partnermain";
    }

    public String commandCustomer() {
        customerBackingBean.reload();
        return "pretty:customermain";
    }

    public String commandProjekte() {
        projectBackingBean.reload();
        return "pretty:projectmain";
    }

    public String commandProfile() {
        profileBackingBean.resetNavigation();
        return "pretty:profilemain";
    }

    public String commandStatistik() {
        return "STATISTIK_STATISTIK";
    }
}