package de.prettytree.yarb.restprovider.infrastructure.security;

import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

//https://github.com/openknowledge/java-ee-8-security-api-example/tree/master/src/main/java/de/openknowledge/samples/javaee8/infrastructure/security
@ApplicationScoped
public class JwtAuthenticationMechanism implements HttpAuthenticationMechanism {

	@Inject
	TokenProvider tokenProvider;

	@Override
	public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
			HttpMessageContext context) throws AuthenticationException {
		if (!context.isProtected()) {
			// unprotected api call
			return context.doNothing();
		}

		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null) {
			return context.responseUnauthorized();
		}

		String[] headerComponents = header.split(" ");
		String tokenString = headerComponents[1];
		Jws<Claims> token = tokenProvider.parseValidToken(tokenString);
		if(token != null) {
			return context.notifyContainerAboutLogin(token.getBody().getSubject(), new HashSet<>(token.getBody().get("groups", List.class)));
		}else {
			return context.responseUnauthorized();
		}
	}
}
