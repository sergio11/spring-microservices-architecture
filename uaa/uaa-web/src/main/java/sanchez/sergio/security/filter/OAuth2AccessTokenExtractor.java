package sanchez.sergio.security.filter;

/**
 * @author Sergio Sánchez
 * To extractor access_token from response of /token
 */
public interface OAuth2AccessTokenExtractor {
    String getAccessTokenValue(byte[] response);
}
