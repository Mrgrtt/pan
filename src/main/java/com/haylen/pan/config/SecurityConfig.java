package com.haylen.pan.config;

import com.haylen.pan.component.AccessDeniedHandlerImpl;
import com.haylen.pan.component.AuthenticationEntryPointImpl;
import com.haylen.pan.component.JwtFilter;
import com.haylen.pan.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

/**
 * @author haylen
 * @date 2019-12-25
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private OwnerService ownerService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/owner/register*", "/owner/login*")
                .permitAll()
                .anyRequest()
                .authenticated();
        http.headers().cacheControl().disable();
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler())
                .authenticationEntryPoint(restAuthenticationEntryPoint());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return name -> {
            UserDetails userDetails = ownerService.getOwnerDetailsByUsername(name);
            if (userDetails == null) {
                throw new UsernameNotFoundException("用户不存在");
            }
            return userDetails;
        };
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Filter jwtFilter() {
        return new JwtFilter();
    }

    @Bean
    public AccessDeniedHandlerImpl restfulAccessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new AuthenticationEntryPointImpl();
    }
}
