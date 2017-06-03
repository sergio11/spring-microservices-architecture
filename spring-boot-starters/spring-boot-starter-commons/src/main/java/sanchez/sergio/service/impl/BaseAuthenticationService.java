
package sanchez.sergio.service.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import sanchez.sergio.service.IAuthenticationService;

/**
 *
 * @author sergio
 */
public abstract class BaseAuthenticationService<T> implements IAuthenticationService<T> {
    
    @Autowired
    private DefaultTokenServices tokenServices;
    
    @Autowired
    private TokenStore tokenStore;
    
    protected Map<String, Object> getTenantInformation(){
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        return tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation();
    }
}
