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

	public String commandFreelancer() {
        return "FREELANCER_STAMMDATEN";
    }

    public String commandPartner() {
        return "PARTNER_STAMMDATEN";
    }

    public String commandCustomer() {
        return "CUSTOMER_STAMMDATEN";
    }

    public String commandProjekte() {
        return "PROJEKT_STAMMDATEN";
    }

    public String commandProfile() {
        return "PROFILE_STAMMDATEN";
    }

    public String commandStatistik() {
        return "STATISTIK_STATISTIK";
    }
}