package de.prettytree.yarb.restprovider.api.infrastructure.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	private static final String BEARER = "Bearer ";
	
	TokenProvider tokenProvider;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, ApplicationContext applicationContext) {
		super(authenticationManager);
		//from GenericFilterBean doc (parent class): 
		//This generic filter base class has no dependency on the Spring
		// * {@link org.springframework.context.ApplicationContext} concept.
		tokenProvider = applicationContext.getBean(TokenProvider.class);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);
		if (authenticationToken != null) {
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);	
		}
		
		chain.doFilter(request, response);
	}

	@SuppressWarnings("unchecked")
	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {

		String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.hasLength(header) && header.startsWith(BEARER)) {
			try {
				String[] headerComponents = header.split(" ");
				String tokenString = headerComponents[1];
				Jws<Claims> token = tokenProvider.parseValidToken(tokenString);

				if (token != null) {
					List<String> groups = token.getBody().get("groups", List.class);
					List<SimpleGrantedAuthority> authorities = groups.stream()
							.map((group) -> new SimpleGrantedAuthority((String) group))
							.collect(Collectors.toList());
					return new UsernamePasswordAuthenticationToken(token.getBody().getSubject(), null, authorities);
				}
			} catch (Exception e) {
				LOG.warn("Unable to validate authorisation header", e);
			}
		}

		return null;
	}
}
