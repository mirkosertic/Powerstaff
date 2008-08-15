package de.mogwai.common.web.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;

import de.mogwai.common.usercontext.Authenticatable;
import de.mogwai.common.usercontext.UserContext;
import de.mogwai.common.usercontext.UserContextHolder;

public class SecurityContextToUserContextFilter extends BaseFilter {

    private static final String USERCONTEXT_ID = "MogwaiUserContext";

    private static final class SecurityContextToAuthenticatableWrapper implements Authenticatable {

        public SecurityContextToAuthenticatableWrapper() {
        }

        /**
         * {@inheritDoc}
         */
        public String getUserId() {

            SecurityContext theContext = SecurityContextHolder.getContext();
            Authentication theAuthentication = theContext.getAuthentication();

            if (theAuthentication != null) {
                Object thePrincipal = theAuthentication.getPrincipal();
                if (thePrincipal instanceof UserDetails) {
                    return ((UserDetails) thePrincipal).getUsername();
                }
                return thePrincipal.toString();
            }
            return null;
        }
    }

    private static final Authenticatable AUTHWRAPPER = new SecurityContextToAuthenticatableWrapper();


    /**
     * {@inheritDoc}
     */
    public void doFilter(ServletRequest aRequest, ServletResponse aResponse, FilterChain aChain) throws IOException,
            ServletException {

        HttpServletRequest theHTTPRequest = (HttpServletRequest) aRequest;
        HttpSession theSession = theHTTPRequest.getSession();

        try {

            UserContext theOldUserContext = (UserContext) theSession.getAttribute(USERCONTEXT_ID);
            UserContext theUserContext = null;
            
            SecurityContext theSecContext = SecurityContextHolder.getContext();
            Authentication theAuth = theSecContext.getAuthentication();
            if (theAuth != null) {
                if (theAuth.getPrincipal() instanceof Authenticatable) {
                    theUserContext = UserContextHolder.initContextWithAuthenticatable((Authenticatable) theAuth
                            .getPrincipal());
                } else {
                    theUserContext = UserContextHolder.initContextWithAuthenticatable(AUTHWRAPPER);
                }
            } else {
                theUserContext = UserContextHolder.initContextWithAuthenticatable(AUTHWRAPPER);
            }

            // Alte Session Variablen in neuen UserContext übernehmen
            if (theOldUserContext != null) {
                for (Map.Entry<Object, Object> theEntry : theOldUserContext.getSessionValues().entrySet()) {
                    theUserContext.setSessionValue(theEntry.getKey(), theEntry.getValue());
                }
            }

            theSession.setAttribute(USERCONTEXT_ID, UserContextHolder.getUserContext());
            
            aChain.doFilter(aRequest, aResponse);

        } finally {
            UserContextHolder.setUserContext(null);
        }
    }


    public void destroy() {
    }


    public void init(FilterConfig aConfig) throws ServletException {
    }
}
