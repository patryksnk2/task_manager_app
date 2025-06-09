package app.task_manager.auth.controller;

import app.task_manager.User.dto.UserDTO;
import app.task_manager.User.dto.UserRegistrationDTO;
import app.task_manager.User.service.UserService;
import app.task_manager.auth.dto.request.AuthRefreshRequest;
import app.task_manager.auth.dto.request.AuthRequest;
import app.task_manager.auth.dto.response.AuthResponse;
import app.task_manager.auth.provider.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegistrationDTO dto) {
        log.info("POST /api/auth/register – starting user registration: username='{}', email='{}'", dto.getUsername(), dto.getEmail());

        UserDTO created = userService.register(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{id}").buildAndExpand(created.getUserId()).toUri();

        log.info("POST /api/auth/register – registration successful, userId={}", created.getUserId());
        return ResponseEntity.created(location).header(HttpHeaders.LOCATION, location.toString()).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
        log.info("POST /api/auth/login – login attempt: username='{}'", req.getUsername());
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.debug("POST /api/auth/login – authentication succeeded for '{}'", req.getUsername());

            String accessToken = jwtTokenProvider.generateAccessToken(auth);
            String refreshToken = jwtTokenProvider.generateRefreshToken(auth);
            UserDTO user = userService.getCurrentUser();

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{id}").buildAndExpand(user.getUserId()).toUri();

            log.info("POST /api/auth/login – tokens generated for userId={}", user.getUserId());
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).header("Refresh-Token", refreshToken).header(HttpHeaders.LOCATION, location.toString()).body(new AuthResponse(accessToken, refreshToken, user));

        } catch (AuthenticationException ex) {
            log.warn("POST /api/auth/login – failed login for '{}': {}", req.getUsername(), ex.getMessage());
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody AuthRefreshRequest request) {
        String rawRefresh = request.getRefreshToken();
        log.info("POST /api/auth/refresh – refresh token request received");

        try {
            jwtTokenProvider.validateRefreshToken(rawRefresh);
            String username = jwtTokenProvider.getUsernameFromJWT(rawRefresh);
            log.debug("POST /api/auth/refresh – extracted username='{}' from refresh token", username);

            UserDetails ud = userDetailsService.loadUserByUsername(username);
            Authentication auth = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());

            String accessToken = jwtTokenProvider.generateAccessToken(auth);
            String refreshToken = jwtTokenProvider.generateRefreshToken(auth);
            UserDTO user = userService.getCurrentUser();

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{id}").buildAndExpand(user.getUserId()).toUri();

            log.info("POST /api/auth/refresh – new tokens issued for userId={}", user.getUserId());
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).header("Refresh-Token", refreshToken).header(HttpHeaders.LOCATION, location.toString()).body(new AuthResponse(accessToken, refreshToken, user));

        } catch (RuntimeException ex) {
            log.error("POST /api/auth/refresh – error refreshing token: {}", ex.getMessage(), ex);
            return ResponseEntity.status(403).build();
        }
    }

}
