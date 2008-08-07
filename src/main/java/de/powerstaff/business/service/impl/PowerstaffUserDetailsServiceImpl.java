package de.powerstaff.business.service.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

import de.powerstaff.business.dao.AuthenticationDAO;

public class PowerstaffUserDetailsServiceImpl implements UserDetailsService {

    private AuthenticationDAO authenticationDAO;
    
    public AuthenticationDAO getAuthenticationDAO() {
        return authenticationDAO;
    }

    public void setAuthenticationDAO(AuthenticationDAO authenticationDAO) {
        this.authenticationDAO = authenticationDAO;
    }

    public UserDetails loadUserByUsername(String aUserName) throws UsernameNotFoundException, DataAccessException {
        return authenticationDAO.findUserByName(aUserName);
    }
}
