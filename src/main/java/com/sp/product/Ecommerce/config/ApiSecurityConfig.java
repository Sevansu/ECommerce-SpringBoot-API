package com.sp.product.Ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sp.product.Ecommerce.service.UserAuthService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ApiSecurityConfig {

	@Autowired
	JwtAuthenticationFilter filter;
	@Autowired
	ApiAuthenticationEntryPoint entryPoint;
	@Autowired
	UserAuthService userDetails;

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//
//	}


	@Bean
	public PasswordEncoder getPasswordEndcoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

    
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
             http.csrf(csrf -> csrf.disable())
            .exceptionHandling(exp -> exp.authenticationEntryPoint(entryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/public/**").permitAll()
            .requestMatchers("/api/auth/consumer/**").hasAnyAuthority("CONSUMER")
            .requestMatchers("/api/auth/seller/**").hasAnyAuthority("SELLER")
            .anyRequest()
            .authenticated());
            
            
            http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
            
            return http.build();
    }
	

//	@Bean
//	public RegistrationBean jwtAuthFilterRegister(JwtAuthenticationFilter filter) {
//		return null;
//	}
}
