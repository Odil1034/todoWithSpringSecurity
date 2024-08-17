package uz.pdp.maven.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    public SpringSecurityConfig(CustomAuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    private static final String[] WHITE_LIST = {
            "/static/**",
            "/auth/login",
            "/auth/signup",
            "/auth/logout",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, CustomUserDetailsService customUserDetailsService) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().authenticated())

                .formLogin((login) -> login
                        .loginPage("/auth/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .failureHandler(authenticationFailureHandler)
                        .defaultSuccessUrl("/main/home", false)
                        .permitAll())

                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST")))

                .rememberMe(remMe -> remMe
                        .rememberMeParameter("remMe")
                        .rememberMeCookieName("r-Me")
                        .alwaysRemember(false)
                        .userDetailsService(customUserDetailsService)
                        .key("fadlkvfoqregad.slorwger;gdvsdfmlsdfqwe")
                        .tokenValiditySeconds(60*60*24));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
