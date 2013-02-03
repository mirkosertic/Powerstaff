package de.powerstaff.web.backingbean;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.powerstaff.business.entity.Contact;
import net.oauth.*;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient3.HttpClient3;
import org.apache.commons.io.IOUtils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Connector zu Xing.
 */
public class XingConnectorBackingBean {

    private static final String SESSIONDATA_SESSION_KEY = "OAuthSessionData";

    private static final String API_BASE = "https://api.xing.com";

    private String consumerKey;

    private String consumerSecret;

    private OAuthServiceProvider serviceProvider;

    private OAuthClient client;

    public <T extends Contact> SocialInfo locatePersonData(Set<T> aContactList) {

        SocialInfo theSocialInfo = new SocialInfo();

        FacesContext theContext = FacesContext.getCurrentInstance();
        HttpServletRequest theHttpRequest = (HttpServletRequest) theContext.getExternalContext().getRequest();
        HttpSession theSession = theHttpRequest.getSession();

        StringBuilder theBuilder = new StringBuilder();
        for (T theContact : aContactList) {
            if (theContact.getType().isEmail()) {
                if (theBuilder.length() > 0) {
                    theBuilder.append(",");
                }
                theBuilder.append(theContact.getValue());
            }
        }

        if (theBuilder.length() > 0) {
            // Es gibt KOntaktinfos.

            // Suche nach Userids anhand der Mail adresse
            Collection<OAuth.Parameter> theParameters = new ArrayList<OAuth.Parameter>();
            theParameters.add(new OAuth.Parameter("emails", theBuilder.toString()));
            try {
                // Zuerst die UserIDs zusammensuchen
                OAuthMessage theResult = client.invoke(getXingAccessor(theSession), "GET", API_BASE + "/v1/users/find_by_emails.json", theParameters);
                Map<String, Object> theJSonResult = new ObjectMapper().readValue(theResult.getBodyAsStream(), HashMap.class);

                StringBuilder theUserIds = new StringBuilder();

                Map theResults = (Map) theJSonResult.get("results");
                List<Map> theItems = (List<Map>) theResults.get("items");
                for (Map theEntry : theItems) {
                    String theMail = (String) theEntry.get("email");
                    Map theUser = (Map) theEntry.get("user");
                    if (theUser != null) {
                        if (theUserIds.length() > 0) {
                            theUserIds.append(",");
                        }
                        theUserIds.append(theUser.get("id"));
                    }
                }

                // Und jetzt die Userinfos
                if (theUserIds.length() > 0) {
                    theSocialInfo.infoProvided = true;

                    theParameters = new ArrayList<OAuth.Parameter>();

                    theResult = client.invoke(getXingAccessor(theSession), "GET", API_BASE + "/v1/users/" + theUserIds, theParameters);
                    theJSonResult = new ObjectMapper().readValue(theResult.getBodyAsStream(), HashMap.class);

                    System.out.println(theJSonResult);

                    List<Map> theUsers = (List<Map>) theJSonResult.get("users");
                    if (theUsers != null) {
                        for (Map theUser : theUsers) {
                            Map thePhotoUrls = (Map) theUser.get("photo_urls");
                            if (thePhotoUrls != null) {
                                String theLargeURL = (String) thePhotoUrls.get("large");
                                if (theLargeURL != null) {
                                    theSocialInfo.setImageUrl(theLargeURL);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return theSocialInfo;
    }

    private static class SessionData {
        OAuthConsumer consumer;
        OAuthAccessor accessor;
        String token;
        String verifier;
        boolean authenticated;
    }

    public XingConnectorBackingBean() {
        serviceProvider = new OAuthServiceProvider(API_BASE + "/v1/request_token", API_BASE + "/v1/authorize", API_BASE + "/v1/access_token");
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