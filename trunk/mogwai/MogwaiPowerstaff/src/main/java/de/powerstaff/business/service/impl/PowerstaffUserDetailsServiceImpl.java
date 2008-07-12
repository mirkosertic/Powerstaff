package de.powerstaff.business.service.impl;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;

public class PowerstaffUserDetailsServiceImpl implements UserDetailsService {

    private String[] authorities;

    private class PowerstaffUserDetails implements UserDetails {

        private String userName;

        public PowerstaffUserDetails(String aUserName) {
            userName = aUserName;
        }

        public GrantedAuthority[] getAuthorities() {
            GrantedAuthority[] theResult = new GrantedAuthority[authorities.length];
            for (int i = 0; i < authorities.length; i++) {
                theResult[i] = new GrantedAuthorityImpl(authorities[i]);
            }
            return theResult;
        }

        public String getPassword() {
            return "";
        }

        public String getUsername() {
            return userName;
        }

        public boolean isAccountNonExpired() {
            return true;
        }

        public boolean isAccountNonLocked() {
            return true;
        }

        public boolean isCredentialsNonExpired() {
            return true;
        }

        public boolean isEnabled() {
            return true;
        }
    }

    public UserDetails loadUserByUsername(String aUserName) {
        return new PowerstaffUserDetails(aUserName);
    }

    /**
     * @return the authorities
     */
    public String[] getAuthorities() {
        return authorities;
    }

    /**
     * @param authorities
     *                the authorities to set
     */
    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }
}
