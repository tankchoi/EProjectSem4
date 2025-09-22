package vn.aptech.java.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import vn.aptech.java.services.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private CustomUserDetailService customUserDetailService;

        @Bean
        @Order(1)
        SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .securityMatcher("/admin/**")
                                .authorizeHttpRequests((auth) -> auth
                                                .requestMatchers("/admin/login").permitAll()
                                                .requestMatchers("/admin/staff/**", "/admin/customer/**")
                                                .hasAuthority("ADMIN")
                                                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN", "STAFF")
                                                .anyRequest().authenticated())

                                .formLogin(login -> login
                                                .loginPage("/admin/login")
                                                .loginProcessingUrl("/admin/login")
                                                .usernameParameter("username")
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/admin/laptop", true))
                                .logout(logout -> logout
                                                .logoutUrl("/admin/logout")
                                                .logoutSuccessUrl("/admin/login?logout"));
                return http.build();
        }

        @Bean
        @Order(2)
        SecurityFilterChain clientSecurityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .securityMatcher(request -> !request.getRequestURI().startsWith("/admin"))
                                .authorizeHttpRequests((auth) -> auth
                                                .requestMatchers("/", "/custom-login", "/register", "/contact",
                                                                "/search-parts", "/images/parts/**", "/images/**",
                                                                "/check-warranty")
                                                .permitAll()
                                                .anyRequest().hasAuthority("CUSTOMER"))
                                .formLogin(login -> login
                                                .loginPage("/custom-login")
                                                .loginProcessingUrl("/custom-login")
                                                .usernameParameter("username")
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/", true))
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/custom-login"));
                return http.build();
        }

        @Bean
        WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring().requestMatchers("/assets/**", "/uploads/**");
        }
}
