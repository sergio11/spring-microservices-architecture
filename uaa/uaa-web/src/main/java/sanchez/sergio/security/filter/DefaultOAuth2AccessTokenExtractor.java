package sanchez.sergio.security.filter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author Sergio Sánchez
 * this is the implementation for extract access_token from response of oauth/token response
 * this will work fine for password flow
 */
public class DefaultOAuth2AccessTokenExtractor implements OAuth2AccessTokenExtractor {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getAccessTokenValue(byte[] response) {
        try {
            return mapper.readValue(response, OAuth2AccessToken.class)
                    .getValue();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
