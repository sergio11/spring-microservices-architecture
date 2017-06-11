package sanchez.sergio.service.impl;

import java.util.Map;
import javax.naming.Name;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import sanchez.sergio.security.userdetails.CommonUserDetailsAware;
import sanchez.sergio.security.userdetails.impl.UserDetailsImpl;

/**
 *
 * @author sergio
 */
@Service
@Profile("prod")
public class LdapAuthenticationServiceImpl extends BaseAuthenticationService<Name> {

    @Override
    public CommonUserDetailsAware<Name> getPrincipal() {
        Map<String, Object> userDetailsInfo = getTenantInformation();
        UserDetailsImpl<Name> userDetails = new UserDetailsImpl<Name>();
        userDetails.setId((Name)userDetailsInfo.get(CommonUserDetailsAware.ID));
        userDetails.setFirstName((String) userDetailsInfo.get(CommonUserDetailsAware.FIRST_NAME));
        userDetails.setLastName((String) userDetailsInfo.get(CommonUserDetailsAware.LAST_NAME));
        userDetails.setUsername((String) userDetailsInfo.get(CommonUserDetailsAware.USERNAME));
        userDetails.setEmail((String) userDetailsInfo.get(CommonUserDetailsAware.EMAIL));
        userDetails.setPassword((String) userDetailsInfo.get(CommonUserDetailsAware.PASSWORD));
        return userDetails;
    }
    
}
