package de.prettytree.yarb.restprovider.api.infrastructure.security;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prettytree.yarb.restprovider.YARBRestProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@ApplicationScoped
public class TokenProvider {

	private static final Logger LOG = LoggerFactory.getLogger(TokenProvider.class);

	private static final Map<String, Object> JWT_STATIC_CLAIMS = new HashMap<>();
	private static final Map<String, Object> JWT_STATIC_HEADERS = new HashMap<>();

	static {
		// https://www.eclipse.org/community/eclipse_newsletter/2017/september/article2.php
		JWT_STATIC_CLAIMS.put("iss", "YARB Restprovider");
		JWT_STATIC_CLAIMS.put("groups", Arrays.asList(YARBRestProvider.USER_GROUP));

		JWT_STATIC_HEADERS.put("typ", "JWT");
		JWT_STATIC_HEADERS.put("alg", "RS256");
	}

	private PrivateKey privateKey;

	@Inject
	@ConfigProperty(name = "de.prettytree.yarb.restprovider.jwt.keyStorePath", defaultValue = "yarb-jwt.keystore")
	private String keyStorePath;

	@Inject
	@ConfigProperty(name = "de.prettytree.yarb.restprovider.jwt.keyStoreEntryName", defaultValue = "yarp jwt")
	private String jwtKeyStoreEntryName;
	
	@Inject
	@ConfigProperty(name = "de.prettytree.yarb.restprovider.jwt.keyStorePassword", defaultValue = "")
	private String jwtKeyStorePassword;
	
	@Inject
	@ConfigProperty(name = "de.prettytree.yarb.restprovider.jwt.expirationTimeMilliseconds", defaultValue = "3600000") //1000 * 60 * 60
	private Long expirationTime;

	@PostConstruct
	public void init() {
		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(keyStorePath);
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(inputStream, null);
			inputStream.close();

			KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection("".toCharArray());
			KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(jwtKeyStoreEntryName, protParam);
			privateKey = privateKeyEntry.getPrivateKey();
		} catch (Exception e) {
			throw new RuntimeException("Could not load private key", e);
		}
	}

	//TODO: TEST
	public String createToken(long userId) {
		// https://github.com/jwtk/jjwt
		// https://en.wikipedia.org/wiki/PKCS_8
		// https://github.com/MicroProfileJWT/eclipse-newsletter-sep-2017				
        JwtBuilder builder = Jwts.builder()
        		.setHeader(JWT_STATIC_HEADERS)
        		.setClaims(JWT_STATIC_CLAIMS)
        		.setSubject(String.valueOf(userId))
        		.setId(UUID.randomUUID().toString())
        		.setIssuedAt(new Date())
        		.setExpiration(new Date((System.currentTimeMillis() + expirationTime)));

        if(LOG.isTraceEnabled()) {
        	LOG.trace("Created token: {}", builder.compact());
        }
        
        return builder
        		.signWith(privateKey, SignatureAlgorithm.RS256)
        		.compact();
	}

	/**
	 * returns a JWT object, if the token is valid
	 * 
	 * @param tokenString The plain JWT string
	 * @return A JWT object, if the token is valid. Otherwise null is returned
	 */
	//TODO: TEST
	public Jws<Claims> parseValidToken(String tokenString) {
		Jws<Claims> retVal = null;
		try {
			retVal = Jwts.parser().setSigningKey(privateKey).parseClaimsJws(tokenString);
		} catch (Exception e) {
			LOG.debug("Could not parse token: ", e);
		}

		return retVal;
	}
}
