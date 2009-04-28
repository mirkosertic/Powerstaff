package de.powerstaff.web.backingbean.navigation;

import de.mogwai.common.web.backingbean.BackingBean;
import de.powerstaff.web.backingbean.customer.CustomerBackingBean;
import de.powerstaff.web.backingbean.freelancer.FreelancerBackingBean;
import de.powerstaff.web.backingbean.partner.PartnerBackingBean;
import de.powerstaff.web.backingbean.profile.ProfileBackingBean;
import de.powerstaff.web.backingbean.project.ProjectBackingBean;
import de.powerstaff.web.backingbean.statistik.StatistikBackingBean;

public class NavigationBackingBean extends BackingBean {

    public String commandFreelancer() {
        forceNavigationResetOfBean(FreelancerBackingBean.class);
        return "FREELANCER_STAMMDATEN";
    }

    public String commandPartner() {
        forceNavigationResetOfBean(PartnerBackingBean.class);
        return "PARTNER_STAMMDATEN";
    }

    public String commandCustomer() {
        forceNavigationResetOfBean(CustomerBackingBean.class);
        return "CUSTOMER_STAMMDATEN";
    }

    public String commandProjekte() {
        forceNavigationResetOfBean(ProjectBackingBean.class);
        return "PROJEKT_STAMMDATEN";
    }

    public String commandProfile() {
        forceNavigationResetOfBean(ProfileBackingBean.class);
        return "PROFILE_STAMMDATEN";
    }

    public String commandStatistik() {
        forceNavigationResetOfBean(StatistikBackingBean.class);
        return "STATISTIK_STATISTIK";
    }
}
