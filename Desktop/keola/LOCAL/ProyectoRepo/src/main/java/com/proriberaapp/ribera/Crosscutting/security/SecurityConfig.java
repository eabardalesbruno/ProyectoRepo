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
                                                                "/api/v1/users/login",
                                                                "/api/v1/users/check-email",
                                                                "/api/v1/offer-type/**",
                                                                "/api/v1/comfort-type/**",
                                                                "/api/v1/feeding/**",
                                                                "/api/v1/quotation/**",
                                                                "/api/v1/quotation-year/**",
                                                                "/api/v1/quotation-room-offer/**",
                                                                "/api/v1/comfort-room-offer-detail/**",
                                                                "/api/v1/bedstype/**",
                                                                "/api/v1/admin/manager/service/**",
                                                                "/api/v1/services/**",
                                                                "/api/v1/room-image/**",
                                                                "/api/v1/room/**",
                                                                "/api/v1/room-offer/**",
                                                                "/api/v1/roomstates/**",
                                                                "/api/v1/getalltypes/**",
                                                                "/api/v1/restaurant/**",
                                                                "/api/v1/entertainment/**",
                                                                "/api/v1/genders/**",
                                                                "/api/v1/getalltypes/**",
                                                                "/api/v1/complaints/**",
                                                                "/api/v1/countries/**",
                                                                "/api/v1/users/**",
                                                                "/api/v1/promoter/**",
                                                                "/api/v1/s3-client/**",
                                                                "/api/v1/booking/save/**",
                                                                "/api/v1/password-reset/**",
                                                                "/api/v1/documenttype/**",
                                                                "/api/v1/payment-method/**",
                                                                "/api/v1/payment-book/**",
                                                                "/api/v1/payment-book/paged/**",
                                                                "/api/v1/payment-book/pagedpayed/**",
                                                                "/api/v1/payment-book/booking-pay/**",
                                                                "/api/v1/booking-pay/**",
                                                                "/api/v1/payment-token/**",
                                                                "/api/v1/paymenttypes/**",
                                                                "/api/v1/paymentsubtypes/**",
                                                                "/api/v1/currencytype/**",
                                                                "/api/v1/token-email/**",
                                                                "/api/v1/points/**",
                                                                "/api/v1/booking/**",
                                                                "/api/v1/bedroom/**",
                                                                "/api/v1/room-detail/**",
                                                                "/api/v1/email/**",
                                                                "/api/v1/refuse-payments/**",
                                                                "/api/v1/cancel-payments/**",
                                                                "/api/excel/**",
                                                                "/api/reports/**",
                                                                "/api/v1/wallet/**",
                                                                "/api/v1/booking/find/**",
                                                                "/api/v1/maintenance/**",
                                                                "api/v1/booking-state/**",
                                                                "api/v1/commission/**",
                                                                "/api/v1/niubiz/**",
                                                                "/api/v1/fullday/**",
                                                                "/api/v1/ticketentryfullday/**",
                                                                "/swagger-doc/**",
                                                                "/api/v1/rewards/**",
                                                                "/api/v1/user-rewards/**",
                                                                "/api/v1/auth/**",
                                                                "/api/internal/**",
                                                                "/api/v1/notification/**",
                                                                "/api/v1/socios/**",
                                                                "/api/v1/membresias/**",
                                                                "/api/v1/checkin/**",
                                                                "/api/v1/fullday-rates")

                                                .permitAll()

                                                .pathMatchers("/api/v1/admin/**").hasRole("SUPER_ADMIN")

                                                .pathMatchers("/api/v1/admin/manager/**").hasAnyRole("ADMIN")
                                                .pathMatchers(GET, "/api/v1/admin/manager/**").hasAnyAuthority("READ")
                                                .pathMatchers(POST, "/api/v1/admin/manager/**").hasAnyAuthority("WRITE")
                                                .pathMatchers(PATCH, "/api/v1/admin/manager/**")
                                                .hasAnyAuthority("UPDATE")
                                                .pathMatchers(DELETE, "/api/v1/admin/manager/**")
                                                .hasAnyAuthority("DELETE")

                                                .anyExchange().authenticated())

                                .securityContextRepository(securityContextRepository)

                                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                                .logout(ServerHttpSecurity.LogoutSpec::disable)
                                .build();
        }

}