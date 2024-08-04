package com.github.GuilhermeBauer16.FitnessTracking.config;

import com.github.GuilhermeBauer16.FitnessTracking.filters.JwtTokenFilter;
import com.github.GuilhermeBauer16.FitnessTracking.filters.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtConfig extends SecurityConfigurerAdapter<
        DefaultSecurityFilterChain, HttpSecurity> {


    private final JwtTokenProvider tokenProvider;

    @Autowired
    public JwtConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
