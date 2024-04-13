package com.proriberaapp.ribera.Crosscutting.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtFilter jwtFilter) {

        return http.authorizeExchange(
                        auth -> auth
                                .pathMatchers(
                                        "/api/v1/admin/login",
                                        "/api/v1/users/**",
                                        "/api/v1/password-reset/**",
                                        "/api/v1/payment-books/**",
                                        "/api/v1/**",
                                        "/swagger-doc/webjars/swagger-ui/swagger-ui-bundle.js",
                                        "/swagger-doc/webjars/swagger-ui/swagger-ui.css",
                                        "/swagger-doc/webjars/swagger-ui/swagger-initializer.js",
                                        "/swagger-doc/webjars/swagger-ui/swagger-ui-standalone-preset.js",
                                        "/swagger-doc/webjars/swagger-ui/swagger-ui.js",
                                        "/swagger-doc/webjars/swagger-ui/swagger-ui.css",
                                        "/swagger-doc/webjars/swagger-ui/**",
                                        "/swagger-doc/webjars/**",
                                        "/swagger-doc/**",
                                        "/api-docs/**",
                                        "/swagger-ui.html",
                                        "/favicon.ico",
                                        "/actuator/**"
                                )
                                .permitAll()

                                .pathMatchers("/api/v1/admin/manager/**").hasRole("SUPER_ADMIN")

                                .pathMatchers("/api/v1/admin/manager/payment/**").hasAnyRole("ADMIN")
                                .pathMatchers(GET, "/api/v1/admin/manager/payment/**").hasAnyAuthority("READ", "WRITE")
                                .pathMatchers(POST, "/api/v1/admin/manager/payment/**").hasAnyAuthority("READ", "WRITE")


                                .anyExchange().authenticated()
                )

                .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.FIRST)
                .securityContextRepository(securityContextRepository)

                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .build();
    }
}