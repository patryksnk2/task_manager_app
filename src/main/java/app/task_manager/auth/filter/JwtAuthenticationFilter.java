package app.task_manager.auth.filter;

import app.task_manager.auth.exception.InvalidJwtAuthenticationException;
import app.task_manager.auth.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        String uri    = request.getRequestURI();
        log.info("Incoming request: {} {}", method, uri);

        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (isBearerTokenMissing(header)) {
            log.debug("No Authorization header present, proceeding without authentication");
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(header);
        log.debug("Bearer token received for {}: {}", uri, token.substring(0, 8) + "...");

        try {
            String username = authenticateByToken(token);
            log.info("Authentication successful for user='{}' on request {}", username, uri);
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            handleAuthenticationFailure(ex, response, uri);
        }
    }


    private String authenticateByToken(String token) {
        jwtTokenProvider.validateAccessToken(token);
        String username = jwtTokenProvider.getUsernameFromJWT(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return username;
    }

    private void handleAuthenticationFailure(Exception ex, HttpServletResponse response, String uri) throws IOException {
        SecurityContextHolder.clearContext();
        log.warn("Authentication failed on {}: {}", uri, ex.getMessage());
        response.addHeader("WWW-Authenticate", "Bearer realm=\"task_manager\"");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }

    private String extractToken(String header) {
        return header.substring(BEARER_PREFIX.length());
    }

    private void proceedFilterChain(FilterChain filterChain, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }

    private boolean isBearerTokenMissing(String header) {
        return header == null || !header.startsWith(BEARER_PREFIX);
    }
}
