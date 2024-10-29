package ru.MaslovArtemy.NauJava.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)

                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/registration")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/registration").permitAll()
                        .requestMatchers("/swagger-ui/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .logout((logout) -> logout.logoutSuccessUrl("/login"))

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/login")
                        )
                );


        return httpSecurity.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/swagger-ui/index.html");
            } else {
                response.sendRedirect("/home");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
