package com.microservice.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;


/**
 * The Class OAuthConfiguration that sets up the OAuth2 single sign on
 * configuration and the web security associated with it.
 */
@Configuration
public class OAuthConfiguration extends WebSecurityConfigurerAdapter {

    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String CSRF_ANGULAR_HEADER_NAME = "X-XSRF-TOKEN";

    /**
     * Define the security that applies to the proxy
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //Allow access to all static resources without authentication
                .antMatchers("/uaa/**", "/**/*.html").permitAll()
                .anyRequest().authenticated()
                .antMatchers(HttpMethod.GET, "/accounts/**").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.OPTIONS, "/accounts/**").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.POST, "/accounts/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PUT, "/accounts/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PATCH, "/accounts/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.DELETE, "/accounts/**").access("#oauth2.hasScope('write')")
                .and().csrf().csrfTokenRepository(provideCSRFTokenRepository())
                .and().addFilterAfter(createCSRFHeaderFilter(), CsrfFilter.class);
    }
    
    
    private Filter createCSRFHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                    HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
                        .getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, CSRF_COOKIE_NAME);
                    String token = csrf.getToken();
                    if (cookie == null || token != null
                            && !token.equals(cookie.getValue())) {
                        cookie = new Cookie(CSRF_COOKIE_NAME, token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    /**
     * Angular sends the CSRF token in a custom header named "X-XSRF-TOKEN"
     * rather than the default "X-CSRF-TOKEN" that Spring security expects.
     * Hence we are now telling Spring security to expect the token in the
     * "X-XSRF-TOKEN" header.<br><br>
     *
     * This customization is added to the <code>csrf()</code> filter.
     * @return
     */
    private CsrfTokenRepository provideCSRFTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName(CSRF_ANGULAR_HEADER_NAME);
        return repository;
    }
}
