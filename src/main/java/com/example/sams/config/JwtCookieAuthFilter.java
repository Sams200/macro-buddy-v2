package com.example.sams.config;

import com.example.sams.service.implementation.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtCookieAuthFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.debug:false}")
    private boolean debugEnabled;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        debug("-------- JWT FILTER START --------");
        debug("Request URI: " + request.getRequestURI());
        debug("Request Method: " + request.getMethod());

        Cookie[] cookies = request.getCookies();
        debug("Cookies present: " + (cookies != null ? "Yes, count: " + cookies.length : "No"));

        if (cookies == null) {
            debug("No cookies found, skipping JWT authentication");
            filterChain.doFilter(request, response);
            return;
        }

        if (debugEnabled) {
            for (Cookie cookie : cookies) {
                debug("Cookie: " + cookie.getName() + " = " +
                        (cookie.getName().equals("accessToken") ?
                                cookie.getValue().substring(0, Math.min(20, cookie.getValue().length())) + "..." :
                                cookie.getValue()));
            }
        }

        Cookie accessTokenCookie = Arrays.stream(cookies)
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        if (accessTokenCookie == null) {
            debug("Access token cookie not found, skipping JWT authentication");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = accessTokenCookie.getValue();
            debug("JWT found, attempting to extract username");

            String username = jwtService.extractUsername(jwt);
            debug("Extracted username: " + (username != null ? username : "null"));

            Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
            debug("Existing authentication: " + (existingAuth != null ? existingAuth : "null"));

            if (username == null) {
                debug("Username is null, skipping authentication");
                filterChain.doFilter(request, response);
                return;
            }

            if (existingAuth != null && existingAuth.isAuthenticated()) {
                debug("User already authenticated, continuing");
                filterChain.doFilter(request, response);
                return;
            }

            debug("Loading user details for: " + username);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            debug("User details loaded: " + userDetails);
            debug("User authorities: " + userDetails.getAuthorities());

            debug("Validating token");
            boolean isValid = jwtService.isTokenValid(jwt, userDetails);
            debug("Token validation result: " + isValid);

            if (isValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                debug("Authentication set in security context");

                // Verify authentication was set
                Authentication newAuth = SecurityContextHolder.getContext().getAuthentication();
                debug("New authentication: " + newAuth);
                debug("New principal class: " + (newAuth != null ? newAuth.getPrincipal().getClass().getName() : "null"));
            } else {
                debug("Token validation failed, no authentication set");
            }

            debug("-------- JWT FILTER END --------");
            filterChain.doFilter(request, response);
        }
        catch (Exception exception) {
            System.err.println("JWT processing failed: " + exception.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }

    private void debug(String message) {
        if (debugEnabled) {
            System.out.println(message);
        }
    }
}