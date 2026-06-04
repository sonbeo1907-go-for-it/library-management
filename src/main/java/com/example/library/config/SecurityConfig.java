package com.example.library.config;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.RoleConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                ApplicationConstants.LOGIN_URL,
                                ApplicationConstants.REGISTER_URL,
                                "/css/**",
                                "/js/**",
                                "/webjars/**",
                                "/uploads/**"
                        ).permitAll()
                        .requestMatchers(
                                ApplicationConstants.BOOK_NEW_URL,
                                ApplicationConstants.BOOK_EDIT_URL,
                                ApplicationConstants.BOOK_DELETE_URL,
                                ApplicationConstants.USER_LIST_URL,
                                ApplicationConstants.USER_NEW_URL,
                                ApplicationConstants.USER_EDIT_URL,
                                ApplicationConstants.USER_SAVE_URL,
                                ApplicationConstants.USER_DELETE_URL,
                                ApplicationConstants.OVERDUE_URL
                        ).hasRole(RoleConstants.ROLE_LIBRARIAN)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(ApplicationConstants.LOGIN_URL)
                        .loginProcessingUrl(ApplicationConstants.LOGIN_URL)
                        .defaultSuccessUrl(ApplicationConstants.HOME_URL, true)
                        .failureUrl(ApplicationConstants.LOGIN_URL + "?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl(ApplicationConstants.LOGOUT_URL)
                        .logoutSuccessUrl(ApplicationConstants.LOGIN_URL + "?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}