package de.powerstaff.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ChangeHeaderFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest theRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse theResponse = (HttpServletResponse) servletResponse;

        InvocationHandler theHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object aProxy, Method aMethod, Object[] args) throws Throwable {
                if (aMethod.getName().equals("getHeader")) {
                    if ("Accept".equalsIgnoreCase((String) args[0])) {
                        // Bug im IE9 und FF10, falscher Accept Header beim Download von Ressourcen
                        return "*/*";
                    }
                }
                return aMethod.invoke(theRequest, args);
            }
        };

        HttpServletRequest theProxy = (HttpServletRequest) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{HttpServletRequest.class}, theHandler);
        filterChain.doFilter(theProxy, theResponse);
    }

    @Override
    public void destroy() {
    }
}
