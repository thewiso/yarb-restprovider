package de.prettytree.yarb.restprovider.api.infrastructure.security;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

//https://github.com/openknowledge/java-ee-8-security-api-example/tree/master/src/main/java/de/openknowledge/samples/javaee8/infrastructure/security
@ApplicationScoped
public class JwtAuthenticationMechanism implements HttpAuthenticationMechanism {

	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationMechanism.class);

	@Inject
	TokenProvider tokenProvider;

	@SuppressWarnings("unchecked")
	@Override
	public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
			HttpMessageContext context) throws AuthenticationException {
		if (!context.isProtected()) {
			// unprotected api call
			return context.doNothing();
		}

		try {
			String header = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (header == null) {
				return context.responseUnauthorized();
			}

			String[] headerComponents = header.split(" ");
			String tokenString = headerComponents[1];
			Jws<Claims> token = tokenProvider.parseValidToken(tokenString);
			if (token != null) {
				return context.notifyContainerAboutLogin(token.getBody().getSubject(),
						new HashSet<>(token.getBody().get("groups", List.class)));
			}
		} catch (Exception e) {
			LOG.debug("Unable to validate authorisation header", e);
		}

		return context.responseUnauthorized();
	}
}
