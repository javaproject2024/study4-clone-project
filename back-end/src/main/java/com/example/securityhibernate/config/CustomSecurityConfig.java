package com.example.securityhibernate.config;

import com.example.securityhibernate.security.CustomAuthentication;
import com.example.securityhibernate.security.CustomFilterJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {

    @Autowired
    private CustomAuthentication authProvider;

    @Autowired
    private CustomFilterJwt customFilterJwt;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().cors()
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers("/api/v1/web/**", "/signup")
                        .permitAll() // Cho phép truy cập
//                    .antMatchers("/", "/signin", "/resources/**", "/static/**", "/oauth/**", "/**/*.css.map", "/**/*.css", "/**/*.js","/**/*.js.map",
//                            "/","/**/*.png","/**/*.jpg", "/**/*.woff2")
//                        .permitAll()
                    .antMatchers("/api/v1/admin/**")
                        .hasRole("ADMIN") // admin mới đc truy cập
                    .antMatchers("/api/v1/manager/**")
                        .hasAnyRole("ADMIN", "MANAGER") // Admin và manager được truy cập
                    .anyRequest().authenticated(); // Cần phải xác thực mới được truy cập

        httpSecurity.addFilterBefore(customFilterJwt, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
