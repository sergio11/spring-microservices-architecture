package sanchez.sergio.security;

import sanchez.sergio.security.userdetails.CommonUserDetailsAware;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 *
 * @author sergio
 */
public class UserDetailsTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        CommonUserDetailsAware<Long> principal = (CommonUserDetailsAware<Long>) authentication.getPrincipal();
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put(CommonUserDetailsAware.ID, principal.getUserId());
        additionalInfo.put(CommonUserDetailsAware.USERNAME, principal.getUsername());
        additionalInfo.put(CommonUserDetailsAware.FULL_NAME, principal.getFullName());
        additionalInfo.put(CommonUserDetailsAware.EMAIL, principal.getEmail());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
