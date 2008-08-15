package de.mogwai.common.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.NDC;

import de.mogwai.common.usercontext.UserContext;
import de.mogwai.common.usercontext.UserContextHolder;
import de.mogwai.common.web.sessionlistener.UserContextWebConstants;

/**
 * Filter, um den aktuellen Benutzer im Log4J NDC zu setzen.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:09:04 $
 */
public class PromoteUserIDToLog4JNDCFilter extends BaseFilter implements UserContextWebConstants {

    /**
     * {@inheritDoc}
     */
    public void init(FilterConfig aConfig) throws ServletException {
        logger.logDebug("init");
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() {
        logger.logDebug("destroy");
    }

    /**
     * {@inheritDoc}
     */
    public void doFilter(ServletRequest aRequest, ServletResponse aResponse, FilterChain aChain) throws IOException,
            ServletException {

        UserContext theContext = UserContextHolder.getUserContext();
        String theUserId = aRequest.getRemoteAddr();
        if ((theContext != null) && (theContext.getAuthenticatable() != null)) {
            theUserId += " / " + theContext.getAuthenticatable().getUserId();
        }

        try {
            NDC.push(theUserId);

            aChain.doFilter(aRequest, aResponse);
        } finally {
            NDC.pop();
        }
    }
}
