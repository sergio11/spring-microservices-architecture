package sanchez.sergio.service.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import sanchez.sergio.security.CommonUserDetailsAware;
import sanchez.sergio.security.UserDetailsImpl;
import sanchez.sergio.service.IAuthenticationService;

/**
 *
 * @author sergio
 */
@Service
public class AuthenticationServiceImpl implements IAuthenticationService {
    
    @Autowired
    private DefaultTokenServices tokenServices;
    
    @Autowired
    private TokenStore tokenStore;
    
    protected Map<String, Object> getTenantInformation(){
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        return tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation();
    }
    
    @Override
    public CommonUserDetailsAware<Long> getPrincipal() {
        Map<String, Object> userDetailsInfo = getTenantInformation();
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setId(new Long( (Integer) userDetailsInfo.get(CommonUserDetailsAware.ID)));
        userDetails.setFirstName((String) userDetailsInfo.get(CommonUserDetailsAware.FIRST_NAME));
        userDetails.setLastName((String) userDetailsInfo.get(CommonUserDetailsAware.LAST_NAME));
        userDetails.setUsername((String) userDetailsInfo.get(CommonUserDetailsAware.USERNAME));
        userDetails.setEmail((String) userDetailsInfo.get(CommonUserDetailsAware.EMAIL));
        userDetails.setPassword((String) userDetailsInfo.get(CommonUserDetailsAware.PASSWORD));
        return userDetails;
    }
    
}
