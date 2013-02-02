package de.powerstaff.web.backingbean;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient3.HttpClient3;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Connector zu Xing.
 */
public class XingConnectorBackingBean {

    private static final String SESSIONDATA_SESSION_KEY = "OAuthSessionData";

    private String consumerKey;

    private String consumerSecret;

    private OAuthServiceProvider serviceProvider;

    private OAuthClient client;

    private static class SessionData {
        OAuthConsumer consumer;
        OAuthAccessor accessor;
        String token;
        String verifier;
        boolean authenticated;
    }

    public XingConnectorBackingBean() {
        String theAPIBase = "https://api.xing.com";
        serviceProvider = new OAuthServiceProvider(theAPIBase + "/v1/request_token", theAPIBase + "/v1/authorize", theAPIBase + "/v1/access_token");
        // Proxy Config nicht vergessen!!
        client = new OAuthClient(new HttpClient3());
    }

    private SessionData getOrCreateSessionData(HttpSession aSession) {
        SessionData theData = (SessionData) aSession.getAttribute(SESSIONDATA_SESSION_KEY);
        if (theData == null) {
            theData = new SessionData();
            aSession.setAttribute(SESSIONDATA_SESSION_KEY, theData);
        }
        return theData;
    }

    public boolean isAuthenticated() {
        FacesContext theContext = FacesContext.getCurrentInstance();
        HttpServletRequest theHttpRequest = (HttpServletRequest) theContext.getExternalContext().getRequest();
        return getOrCreateSessionData(theHttpRequest.getSession()).authenticated;
    }

    private OAuthConsumer getXingConsumer(HttpSession aSession) {
        SessionData theData = getOrCreateSessionData(aSession);
        OAuthConsumer theConsumer = theData.consumer;
        if (theConsumer == null) {
            theConsumer = new OAuthConsumer(null, consumerKey, consumerSecret, serviceProvider);
            theData.consumer = theConsumer;
        }
        return theConsumer;
    }

    private OAuthAccessor getXingAccessor(HttpSession aSession) {

        SessionData theData = getOrCreateSessionData(aSession);
        OAuthAccessor theAccessor = theData.accessor;
        if (theAccessor == null) {
            theAccessor = new OAuthAccessor(getXingConsumer(aSession));
            theData.accessor = theAccessor;
        }
        return theAccessor;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public void performCallback() {
        FacesContext theContext = FacesContext.getCurrentInstance();
        HttpServletRequest theHttpRequest = (HttpServletRequest) theContext.getExternalContext().getRequest();
        HttpServletResponse theHttpResponse = (HttpServletResponse) theContext.getExternalContext().getResponse();

        SessionData theData = getOrCreateSessionData(theHttpRequest.getSession());
        theData.token = theHttpRequest.getParameter(OAuth.OAUTH_TOKEN);
        theData.verifier = theHttpRequest.getParameter(OAuth.OAUTH_VERIFIER);

        try {
            client.getAccessToken(theData.accessor, null, OAuth.newList(OAuth.OAUTH_VERIFIER, theData.verifier));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        theData.authenticated = true;

        StringBuffer theRequestUrl = theHttpRequest.getRequestURL();
        int p = theRequestUrl.lastIndexOf("/");
        theRequestUrl = theRequestUrl.replace(p, theRequestUrl.length(), "/freelancer/new/main");

        String theRedirectURL = theHttpResponse.encodeRedirectURL(theRequestUrl.toString());

        try {
            theHttpResponse.sendRedirect(theRedirectURL);
            theContext.responseComplete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void performXingAuthentication() {

        try {
            FacesContext theContext = FacesContext.getCurrentInstance();
            HttpServletRequest theHttpRequest = (HttpServletRequest) theContext.getExternalContext().getRequest();
            HttpServletResponse theHttpResponse = (HttpServletResponse) theContext.getExternalContext().getResponse();


            OAuthAccessor theAccessor = getXingAccessor(theHttpRequest.getSession());

            List<OAuth.Parameter> theParameters = new ArrayList<OAuth.Parameter>();

            StringBuffer theRequestUrl = theHttpRequest.getRequestURL();
            int p = theRequestUrl.lastIndexOf("/");
            theRequestUrl = theRequestUrl.replace(p, theRequestUrl.length(), "/xingcallback");

            String theRedirectURL = theHttpResponse.encodeRedirectURL(theRequestUrl.toString());

            theParameters.add(new OAuth.Parameter(OAuth.OAUTH_CALLBACK, theRedirectURL));

            client.getRequestTokenResponse(theAccessor, null, theParameters);

            String theAutorizationURL = OAuth.addParameters(theAccessor.consumer.serviceProvider.userAuthorizationURL, OAuth.OAUTH_TOKEN, theAccessor.requestToken);

            theHttpResponse.sendRedirect(theAutorizationURL);

            theContext.responseComplete();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}