package de.mogwai.common.web.filter;

import de.mogwai.common.usercontext.Authenticatable;
import de.mogwai.common.usercontext.UserContext;
import de.mogwai.common.usercontext.UserContextHolder;
import org.apache.log4j.NDC;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter, um den aktuellen Benutzer im Log4J NDC zu setzen.
 *
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:19:08 $
 */
public class PromoteUserIDToLog4JNDCFilter implements Filter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PromoteUserIDToLog4JNDCFilter.class);

    public void init(FilterConfig aConfig) throws ServletException {
        LOGGER.info("init");
    }

    public void destroy() {
        LOGGER.info("destroy");
    }

    public void doFilter(ServletRequest aRequest, ServletResponse aResponse, FilterChain aChain) throws IOException,
            ServletException {

        UserContext theContext = UserContextHolder.getUserContext();
        String theUserId = aRequest.getRemoteAddr();
        if ((theContext != null) && (theContext.getAuthenticatable() != null)) {
            Authenticatable theUser = theContext.getAuthenticatable();
            theUserId += " / " + theUser.getUserId();
        }

        try {
            NDC.push(theUserId);

            aChain.doFilter(aRequest, aResponse);
        } finally {
            NDC.pop();
        }
    }
}
