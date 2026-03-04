package com.example.milk_order_app.config;

import com.example.milk_order_app.jwt.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtFilter;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        return http
                .csrf(csrf -> csrf.disable())

                // ✅ Enable CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ✅ Stateless (JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // ✅ Authorization Rules
                .authorizeHttpRequests(auth -> auth

                        // AUTH APIs
                        .requestMatchers("/auth/**").permitAll()

                        // DASHBOARD
                        .requestMatchers("/dashboard/**").permitAll()
                        .requestMatchers("/products/summary").permitAll()
                        .requestMatchers("/orders/summary").permitAll()
                        .requestMatchers("/users/summary").permitAll()

                        // PRODUCTS
                        .requestMatchers("/products/saveproduct").hasRole("ADMIN")
                        .requestMatchers("/products/updating/**").hasRole("ADMIN")
                        .requestMatchers("/products/deleting/**").hasRole("ADMIN")
                        .requestMatchers("/products/getallproducts")
                        .hasAnyRole("ADMIN", "USER")

                        // ORDERS
                        .requestMatchers("/orders/create")
                        .hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/orders/getallorders")
                        .hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/orders/updateorder/**")
                        .hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/orders/**")
                        .authenticated()

                        .anyRequest().authenticated()
                )

                // ✅ Exception Handling (IMPORTANT)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )

                // ✅ Add JWT Filter
                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    // ✅ CORS Configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // ✅ Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}