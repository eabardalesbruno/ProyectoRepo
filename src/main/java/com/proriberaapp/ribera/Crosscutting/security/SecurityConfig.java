package com.proriberaapp.ribera.Crosscutting.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)

                .authorizeExchange(auth -> auth
                        .pathMatchers(OPTIONS).permitAll()

                        .pathMatchers("/api/v1/admin/login",
                                "/api/v1/users/**",
                                "/api/v1/s3-client/**",
                                "/api/v1/password-reset/**",
                                "/api/documenttype/**",
                                "/api/v1/payment-method/**",
                                "/api/v1/payment-book/**",
                                "/api/v1/payment-book/booking-pay/**",
                                "/api/v1/booking-pay/**",
                                "/api/v1/payment-token/**",
                                "/api/v1/paymenttypes/**",
                                "/api/v1/paymentsubtypes/**",
                                "/api/v1/currencytype/**",
                                "/api/v1/token-email/**",
                                "/api/v1/points/**",
                                "/api/v1/booking/**",
                                "/api/v1/email/**",
                                "/api/v1/refuse-payments/**",
                                "/swagger-doc/**"
                        ).permitAll()

                        .pathMatchers("/api/v1/admin/**").hasRole("SUPER_ADMIN")

                        .pathMatchers("/api/v1/admin/manager/**").hasAnyRole("ADMIN")
                        .pathMatchers(GET, "/api/v1/admin/manager/**").hasAnyAuthority("READ")
                        .pathMatchers(POST, "/api/v1/admin/manager/**").hasAnyAuthority("WRITE")
                        .pathMatchers(PATCH, "/api/v1/admin/manager/**").hasAnyAuthority("UPDATE")
                        .pathMatchers(DELETE, "/api/v1/admin/manager/**").hasAnyAuthority("DELETE")

                        .anyExchange().authenticated()
                )

                .securityContextRepository(securityContextRepository)

                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)

                .build();
    }

}