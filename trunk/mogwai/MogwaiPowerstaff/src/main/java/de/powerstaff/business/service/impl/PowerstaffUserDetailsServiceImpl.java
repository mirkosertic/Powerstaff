package de.powerstaff.business.service.impl;

import de.powerstaff.business.dao.AuthenticationDAO;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;

public class PowerstaffUserDetailsServiceImpl implements UserDetailsService {

    private AuthenticationDAO authenticationDAO;

    public AuthenticationDAO getAuthenticationDAO() {
        return authenticationDAO;
    }

    public void setAuthenticationDAO(AuthenticationDAO authenticationDAO) {
        this.authenticationDAO = authenticationDAO;
    }

    public UserDetails loadUserByUsername(String aUserName) {
        return authenticationDAO.findUserByName(aUserName);
    }
}
