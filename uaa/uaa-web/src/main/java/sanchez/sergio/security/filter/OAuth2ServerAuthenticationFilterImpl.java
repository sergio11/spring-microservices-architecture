package sanchez.sergio.security.filter;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author Sergio Sánchez
 * Authentication handler for server.
 *
 */
@Component
public class OAuth2ServerAuthenticationFilterImpl extends OAuth2ServerAuthenticationFilter {

    private static final Logger logger = Logger.getLogger(OAuth2ServerAuthenticationFilterImpl.class);
    
    @Autowired
    private TokenStore tokenStore;

    public OAuth2ServerAuthenticationFilterImpl(DefaultTokenServices tokenServices) {
        super(tokenServices);
    }

    @Override
    protected void onSuccessfulUserAuthentication(ServletRequest request,
            ServletResponse response, Authentication clientAuthentication,
            OAuth2Authentication userOAuth2Authentication,
            OAuth2AccessToken token) {

        // To skip refresh_token request
        if (userOAuth2Authentication.getOAuth2Request()
                .getRefreshTokenRequest() == null) {

            Authentication userAuthentication = (Authentication) userOAuth2Authentication.getUserAuthentication();
            if(userAuthentication != null) {
                
                logger.debug("on Successful User Authentication- " + userAuthentication.getName());
            }
        }
    }

    @Override
    protected void onFailureUserAuthentication(ServletRequest request,
            ServletResponse response, Authentication clientAuthentication,
            String username) {
        logger.debug("on fail user authenticaion -" + username);
    }

    @Override
    protected void onFailClientAuthentication(ServletRequest request,
            ServletResponse response, String clientId) {
        logger.debug("on fail Client Authentication -" + clientId);
    }

    @Override
    protected void onSuccessfulClientAuthentication(ServletRequest request,
            ServletResponse response, Authentication authentication) {
        logger.debug("on Successful Client Authentication -" + authentication);
    }

    private String extractIp(ServletRequest request) {
        String ipAddress = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            ipAddress = req.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
        }
        return ipAddress;
    }

    private String extractUserAgent(ServletRequest request) {
        String userAgent = null;
        if (request instanceof HttpServletRequest) {
            userAgent = ((HttpServletRequest) request).getHeader("User-Agent")
                    .toLowerCase();
        }
        return userAgent;
    }
    
    @PostConstruct
    public void init(){
        logger.info("Init OAuth2Server Authentication Filter ...");
        Assert.state(tokenStore != null, "A TokenStore must be provided");
    }

}
