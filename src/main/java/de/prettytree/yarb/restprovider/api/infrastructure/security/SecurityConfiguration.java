package de.prettytree.yarb.restprovider.api.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	//https://dev.to/keysh/spring-security-with-jwt-3j76
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
				.and().csrf()
				.disable().authorizeRequests()
				.antMatchers(HttpMethod.POST , "/users").permitAll()
				.antMatchers("/auth/**").permitAll()
				.anyRequest().authenticated()
				.and().addFilter(new JwtAuthenticationFilter(authenticationManager(), getApplicationContext()))
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		
		//TODO: /yarb/boards/* 
		//TODO: /yarb/users/*
	}
	//TODO: as property file?
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

		return source;
	}
}