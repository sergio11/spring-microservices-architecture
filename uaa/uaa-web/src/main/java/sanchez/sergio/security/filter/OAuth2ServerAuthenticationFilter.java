package sanchez.sergio.security.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.output.TeeOutputStream;
import org.apache.log4j.Logger;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

/**
 * @author Sergio Sánchez
 * Filter to handle authentication of server
 *
 */
public abstract class OAuth2ServerAuthenticationFilter implements Filter {

    private static Logger log = Logger
            .getLogger(OAuth2ServerAuthenticationFilter.class);

    private DefaultTokenServices tokenServices;

    private OAuth2AccessTokenExtractor tokenExtractor;

    public OAuth2ServerAuthenticationFilter(DefaultTokenServices tokenServices) {
        this.tokenServices = tokenServices;
        this.tokenExtractor = new DefaultOAuth2AccessTokenExtractor();
    }

    public OAuth2ServerAuthenticationFilter(DefaultTokenServices tokenServices,
            OAuth2AccessTokenExtractor tokenExtractor) {
        this.tokenServices = tokenServices;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        // create wrapper to read response body
        ByteArrayResponseWrapper responseWraper = new ByteArrayResponseWrapper(
                response);

        // led them go
        chain.doFilter(request, responseWraper);

        // get ClientAuthentication
        Authentication clientAuthentication = SecurityContextHolder
                .getContext().getAuthentication();

        // is authenticated or not to proceed
        if (clientAuthentication != null
                && clientAuthentication.isAuthenticated()) {

            // callBack client authenticated successfully
            onSuccessfulClientAuthentication(request, response,
                    clientAuthentication);

            // check response status is success of failure
            if (responseWraper.getStatus() == 200) {

                // extract accessToken from response
                String token = tokenExtractor
                        .getAccessTokenValue(responseWraper.getByteArray());

                if (token != null && !token.isEmpty()) {

                    // load authentication from token
                    OAuth2Authentication oAuth2Authentication = this.tokenServices
                            .loadAuthentication(token);
                    OAuth2AccessToken actualAccessToken = this.tokenServices
                            .getAccessToken(oAuth2Authentication);

                    // callBack user authenticated successfully
                    onSuccessfulUserAuthentication(request, response,
                            clientAuthentication, oAuth2Authentication,
                            actualAccessToken);
                } else {
                    log.error("access token is empty from extractor");
                }
            } else {
                // callBack user authenticated failure
                onFailureUserAuthentication(request, response,
                        clientAuthentication, request.getParameter("username"));
            }
        } else {
            // callBack client authenticated failure
            onFailClientAuthentication(request, response,
                    request.getParameter(OAuth2Utils.CLIENT_ID));
        }
    }

    protected void onSuccessfulClientAuthentication(ServletRequest request,
            ServletResponse response, Authentication authentication) {
    }

    protected void onFailClientAuthentication(ServletRequest request,
            ServletResponse response, String clientId) {
    }

    protected void onSuccessfulUserAuthentication(ServletRequest request,
            ServletResponse response, Authentication clientAuthentication,
            OAuth2Authentication userOAuth2Authentication,
            OAuth2AccessToken token) {
    }

    protected void onFailureUserAuthentication(ServletRequest request,
            ServletResponse response, Authentication clientAuthentication,
            String username) {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    public DefaultTokenServices getTokenServices() {
        return tokenServices;
    }

    public void setTokenServices(DefaultTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    public OAuth2AccessTokenExtractor getTokenExtractor() {
        return tokenExtractor;
    }

    public void setTokenExtractor(OAuth2AccessTokenExtractor tokenExtractor) {
        this.tokenExtractor = tokenExtractor;
    }

    // response wrapper
    private class ByteArrayResponseWrapper extends HttpServletResponseWrapper {

        public ByteArrayResponseWrapper(ServletResponse response) {
            super((HttpServletResponse) response);
        }

        private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new DelegatingServletOutputStream(new TeeOutputStream(
                    super.getOutputStream(), byteArrayOutputStream));
        }

        public byte[] getByteArray() {
            return this.byteArrayOutputStream.toByteArray();
        }
    }
}
