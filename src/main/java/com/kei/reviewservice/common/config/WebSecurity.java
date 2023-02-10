package com.kei.reviewservice.common.config;

import com.kei.reviewservice.business.user.entity.UserRepository;
import com.kei.reviewservice.business.user.service.UserService;
import com.kei.reviewservice.common.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private BCryptPasswordEncoder passwordEncoder;
    private TokenProvider tokenProvider;
    private UserRepository userRepository;
    private UserService userService;

    public WebSecurity(BCryptPasswordEncoder passwordEncoder, TokenProvider tokenProvider,
                       UserRepository userRepository, UserService userService)
    {
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable()
                .and()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager(), tokenProvider, userService))
                .addFilter(new AuthorizationFilter(authenticationManager(), userRepository, tokenProvider))
                .authorizeRequests()
                .antMatchers("/**").permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
