package vn.aptech.java.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import vn.aptech.java.services.CustomUserDetailService;
import vn.aptech.java.configs.PasswordEncoderConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private CustomUserDetailService customUserDetailService;
        // @Bean
        // BCryptPasswordEncoder passwordEncoder() {
        // return new BCryptPasswordEncoder();
        // }
        @Autowired
        private PasswordEncoder passwordEncoder;

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests((auth) -> auth
                                                .requestMatchers("/admin/login", "/client/login", "/assets/**",
                                                                "/images/**", "/css/**", "/js/**")
                                                .permitAll()
                                                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN", "STAFF")
                                                .requestMatchers("/client/**")
                                                .hasAnyAuthority("CUSTOMER", "ADMIN", "STAFF")
                                                .anyRequest().authenticated())
                                .formLogin(login -> login
                                                .loginPage("/admin/login") // Default login page for admin
                                                .loginProcessingUrl("/admin/login")
                                                .usernameParameter("username")
                                                .passwordParameter("password")
                                                .successHandler(customAuthenticationSuccessHandler())
                                                .failureUrl("/admin/login?error=true"))
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/client/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"))
                                .exceptionHandling(exceptions -> exceptions
                                                .accessDeniedPage("/admin/login?error=access_denied"));
                return http.build();
        }

        @Bean
        public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
                return new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
                                String redirectURL = request.getContextPath();

                                if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) ||
                                                authentication.getAuthorities()
                                                                .contains(new SimpleGrantedAuthority("STAFF"))) {
                                        redirectURL += "/admin/homepage";
                                } else if (authentication.getAuthorities()
                                                .contains(new SimpleGrantedAuthority("CUSTOMER"))) {
                                        redirectURL += "/client/homepage";
                                } else {
                                        redirectURL += "/client/homepage"; // Default
                                }

                                response.sendRedirect(redirectURL);
                        }
                };
        }

        // Remove the deprecated webSecurityCustomizer - static resources are now
        // handled in the main SecurityFilterChain
        // @Bean
        // WebSecurityCustomizer webSecurityCustomizer() {
        // return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**",
        // "/images/**");
        // }
}