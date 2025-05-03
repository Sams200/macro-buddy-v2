package com.example.sams.config;

import com.example.sams.enumeration.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtCookieAuthFilter jwtCookieAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/v1/authentication/sign-up", "/api/v1/authentication/sign-in")
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/ws/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/hello-world").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/csrf").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/food").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/food/id=**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/food/name=**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/authentication/sign-up").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/authentication/sign-in").permitAll()


                        .requestMatchers(HttpMethod.POST, "/api/v1/authentication/sign-out").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/v1/users/authenticated").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/authenticated/change-password").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/v1/authenticated/user-settings").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/authenticated/user-settings").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/v1/authenticated/entry").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/authenticated/entry").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/authenticated/entry/date=**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/authenticated/entry/id=**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/authenticated/entry/id=**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/v1/authenticated/water").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/authenticated/water").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/authenticated/water/date=**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/authenticated/water/id=**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/authenticated/water/id=**").authenticated()

                        .anyRequest().hasAnyAuthority(Role.ROLE_ADMIN.name())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtCookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}