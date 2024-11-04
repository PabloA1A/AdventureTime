package dev.pabloabad.adventuretime.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import dev.pabloabad.adventuretime.encryptations.Base64Encoder;
import dev.pabloabad.adventuretime.users.JpaUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Value("${api-endpoint}")
        String endpoint;

        private final JpaUserDetailsService jpaUserDetailsService;
        private final MyBasicAuthenticationEntryPoint myBasicAuthenticationEntryPoint;

        public SecurityConfig(JpaUserDetailsService jpaUserDetailsService,
                        MyBasicAuthenticationEntryPoint basicEntryPoint) {
                this.jpaUserDetailsService = jpaUserDetailsService;
                this.myBasicAuthenticationEntryPoint = basicEntryPoint;
        }

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                .cors(cors -> cors.configurationSource(corsConfiguration()))
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .logout(out -> out
                    .logoutUrl(endpoint + "/logout")
                    .deleteCookies("ADVENTURER"))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.POST, "/api/v1/register").permitAll() 
                    .requestMatchers(HttpMethod.GET, "/api/v1/login").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated())
                .userDetailsService(jpaUserDetailsService)
                .httpBasic(basic -> basic.authenticationEntryPoint(myBasicAuthenticationEntryPoint))
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        
            http.headers(header -> header.frameOptions(frame -> frame.sameOrigin()));
        
            return http.build();
        }

        @Bean
        CorsConfigurationSource corsConfiguration() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowCredentials(true);
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        Base64Encoder base64Encoder() {
                return new Base64Encoder();
        }
}
