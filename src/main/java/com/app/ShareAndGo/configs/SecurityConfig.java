package com.app.ShareAndGo.configs;

import com.app.ShareAndGo.configs.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .headers(headers -> headers.cacheControl(HeadersConfigurer.CacheControlConfig::disable))
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(userDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/admin/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/sup-admin/create-admin").hasAuthority("SUPERADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/user/sign-up", "/api/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/trip/create-trip",
                                "/api/trip/3-latest",
                                "/api/preference/choose-preferences",
                                "/api/car/add-car",
                                "/api/withdrawal/withdraw",
                                "/api/recharge",
                                "/api/trip-application/apply-to-reserve",
                                "/api/feedback/review/leave-review"
                        ).hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/withdrawal/admin/pending-withdrawals").hasAnyAuthority("ADMIN", "SUPERADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/car/delete-car").hasAuthority("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/withdrawal/admin/confirm-withdrawal", "/api/withdrawal/admin/reject-withdrawal").hasAnyAuthority("ADMIN", "SUPERADMIN")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/booking/reject-application",
                                "/api/booking/confirm-application",
                                "/api/trip/cancel-trip",
                                "/api/user/update-profile",
                                "/api/trip/pay-for-trip",
                                "/api/trip/finish-trip"
                        ).hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET,
                                "/api/trip/**",
                                "/api/preference/preferences-by-trip-id",
                                "/api/user/auth-user",
                                "/api/car/all-cars",
                                "/api/feedback/review/by-trip",
                                "/api/withdrawal/all-withdrawals",
                                "/api/trip-application/**",
                                "/api/booking/by-trip"
                        ).hasAuthority("USER")
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-resources").permitAll()
                )

                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

