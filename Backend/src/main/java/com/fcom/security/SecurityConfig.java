package com.fcom.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtAuthEntryPoint authEntryPoint;
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }

    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .requestMatchers(HttpMethod.POST, "/seller/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/admin/alogin").permitAll()
                .requestMatchers(HttpMethod.POST, "/seller/declaration").permitAll()
                .requestMatchers(HttpMethod.POST, "/seller/recharge").permitAll()
                .requestMatchers(HttpMethod.POST, "/seller/tpe").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/save").permitAll()
                .requestMatchers(HttpMethod.GET, "/admin/sellers").permitAll()
                .requestMatchers(HttpMethod.GET, "/seller/sellers/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/report/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/admin/states").permitAll()
                .requestMatchers(HttpMethod.GET, "/today/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/aujour/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/recharge/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/tpe/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/aujour1/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/generate-sales-report/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/sales-report").permitAll()
                .requestMatchers(HttpMethod.POST, "/seller/sales/update").permitAll()
                .requestMatchers(HttpMethod.POST, "/seller/update").permitAll()
                .requestMatchers(HttpMethod.POST, "/seller/recharge/update").permitAll()
                .requestMatchers(HttpMethod.POST, "/seller/tpe/update").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/seller/declaration/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/seller/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(
                Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
