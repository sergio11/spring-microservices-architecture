package sanchez.sergio.service.impl;

import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import sanchez.sergio.security.userdetails.CommonUserDetailsAware;
import sanchez.sergio.security.userdetails.impl.UserDetailsImpl;

/**
 *
 * @author sergio
 */
@Service
@Profile("dev")
public class AuthenticationServiceImpl extends BaseAuthenticationService<Long> {
    
    @Override
    public CommonUserDetailsAware<Long> getPrincipal() {
        Map<String, Object> userDetailsInfo = getTenantInformation();
        UserDetailsImpl<Long> userDetails = new UserDetailsImpl<Long>();
        userDetails.setId(new Long( (Integer) userDetailsInfo.get(CommonUserDetailsAware.ID)));
        userDetails.setFullName((String) userDetailsInfo.get(CommonUserDetailsAware.FULL_NAME));
        userDetails.setUsername((String) userDetailsInfo.get(CommonUserDetailsAware.USERNAME));
        userDetails.setEmail((String) userDetailsInfo.get(CommonUserDetailsAware.EMAIL));
        userDetails.setPassword((String) userDetailsInfo.get(CommonUserDetailsAware.PASSWORD));
        return userDetails;
    }
    
}