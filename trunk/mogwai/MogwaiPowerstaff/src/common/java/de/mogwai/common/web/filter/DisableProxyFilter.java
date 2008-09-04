package de.mogwai.common.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Ausschalten des Proxy Cachings.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:19:13 $
 */
public class DisableProxyFilter extends BaseFilter {

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

        HttpServletResponse theResponse = (HttpServletResponse) aResponse;

        theResponse.addHeader("Pragma", "private");
        theResponse.addDateHeader("Expires", 0);

        aChain.doFilter(aRequest, aResponse);
    }
}
