package sanchez.sergio.config.security;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import sanchez.sergio.security.userdetails.CommonUserDetailsAware;

/**
 *
 * @author sergio
 */
@Component
public class UserDetailsTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        CommonUserDetailsAware<Long> principal = (CommonUserDetailsAware<Long>) authentication.getPrincipal();
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put(CommonUserDetailsAware.USERNAME, principal.getUsername());
        additionalInfo.put(CommonUserDetailsAware.FIRST_NAME, principal.getFirstName());
        additionalInfo.put(CommonUserDetailsAware.LAST_NAME, principal.getLastName());
        additionalInfo.put(CommonUserDetailsAware.EMAIL, principal.getEmail());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
    
}
