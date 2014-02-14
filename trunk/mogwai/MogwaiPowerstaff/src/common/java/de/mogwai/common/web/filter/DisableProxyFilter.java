package de.mogwai.common.web.filter;

import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Ausschalten des Proxy Cachings.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:19:13 $
 */
public class DisableProxyFilter implements Filter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DisableProxyFilter.class);

    /**
     * {@inheritDoc}
     */
    public void init(FilterConfig aConfig) throws ServletException {
        LOGGER.info("init");
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() {
        LOGGER.info("destroy");
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
