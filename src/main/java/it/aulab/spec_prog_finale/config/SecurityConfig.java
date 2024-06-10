package it.aulab.spec_prog_finale.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import it.aulab.spec_prog_finale.services.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((authorize) ->
            authorize.requestMatchers("/register/**").permitAll()
            .requestMatchers("/", "/articles").permitAll()
            .anyRequest().authenticated()
        ).formLogin(form ->
            form.loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/")
            .permitAll()
        ).logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .permitAll()
        );
        
        return http.build();
    }
    
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) 
    throws Exception {
        auth.userDetailsService(customUserDetailsService)
        .passwordEncoder(passwordEncoder);
    } 
}