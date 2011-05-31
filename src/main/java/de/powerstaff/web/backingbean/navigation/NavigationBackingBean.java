package de.powerstaff.web.backingbean.navigation;

import de.mogwai.common.web.backingbean.BackingBean;
import de.powerstaff.web.backingbean.customer.CustomerBackingBean;
import de.powerstaff.web.backingbean.freelancer.FreelancerBackingBean;
import de.powerstaff.web.backingbean.partner.PartnerBackingBean;
import de.powerstaff.web.backingbean.profile.ProfileBackingBean;
import de.powerstaff.web.backingbean.project.ProjectBackingBean;
import de.powerstaff.web.backingbean.statistik.StatistikBackingBean;

public class NavigationBackingBean extends BackingBean {

	private static final long serialVersionUID = -2068461554004949119L;

    private FreelancerBackingBean freelancerBackingBean;
    private PartnerBackingBean partnerBackingBean;
    private ProjectBackingBean projectBackingBean;
    private CustomerBackingBean customerBackingBean;

    public FreelancerBackingBean getFreelancerBackingBean() {
        return freelancerBackingBean;
    }

    public void setFreelancerBackingBean(FreelancerBackingBean freelancerBackingBean) {
        this.freelancerBackingBean = freelancerBackingBean;
    }

    public PartnerBackingBean getPartnerBackingBean() {
        return partnerBackingBean;
    }

    public void setPartnerBackingBean(PartnerBackingBean partnerBackingBean) {
        this.partnerBackingBean = partnerBackingBean;
    }

    public ProjectBackingBean getProjectBackingBean() {
        return projectBackingBean;
    }

    public void setProjectBackingBean(ProjectBackingBean projectBackingBean) {
        this.projectBackingBean = projectBackingBean;
    }

    public CustomerBackingBean getCustomerBackingBean() {
        return customerBackingBean;
    }

    public void setCustomerBackingBean(CustomerBackingBean customerBackingBean) {
        this.customerBackingBean = customerBackingBean;
    }

    public String commandFreelancer() {
        freelancerBackingBean.reload();
        return "FREELANCER_STAMMDATEN";
    }

    public String commandPartner() {
        partnerBackingBean.reload();
        return "PARTNER_STAMMDATEN";
    }

    public String commandCustomer() {
        customerBackingBean.reload();
        return "CUSTOMER_STAMMDATEN";
    }

    public String commandProjekte() {
        return "PROJEKT_STAMMDATEN";
    }

    public String commandProfile() {
        projectBackingBean.reload();
        return "PROFILE_STAMMDATEN";
    }

    public String commandStatistik() {
        return "STATISTIK_STATISTIK";
    }
}