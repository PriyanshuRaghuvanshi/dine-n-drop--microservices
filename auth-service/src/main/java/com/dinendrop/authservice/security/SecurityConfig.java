package com.dinendrop.authservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private JwtAuthenticationEntryPoint point;


    private JwtAuthenticationFilter filter;

    public SecurityConfig(JwtAuthenticationFilter filter , JwtAuthenticationEntryPoint point) {
        this.filter = filter;
        this.point = point;

    }

        @Bean
        public SecurityFilterChain securityFilterChainfilter(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                    .cors(cors -> cors.disable())
                    .authorizeHttpRequests(
                            auth ->
                                    auth.requestMatchers("/auth/**").permitAll()
                                            .requestMatchers("/order/**","/search/**","/hello").authenticated()
                                        .anyRequest().authenticated())
                    .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    ;
            http.addFilterBefore(filter , UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

//        @Bean
//        public UserDetailsService userDetailsService(){
//            UserDetails user1= User.builder().username("Priyanshu").password(passwordEncoder().encode("password123")).roles("ADMIN").build();
//            UserDetails user2= User.builder().username("Lakshay").password(passwordEncoder().encode("pass123")).build();
//            return new InMemoryUserDetailsManager(user1,user2);
//        }
//
//        @Bean
//        public PasswordEncoder passwordEncoder(){
//            return new BCryptPasswordEncoder();
//        }

        @Bean
       public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
       }
}

