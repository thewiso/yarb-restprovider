package de.prettytree.yarb.restprovider.infrastructure.security;

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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Tobias
 *
 */
/**
 * @author Tobias
 *
 */
@ApplicationScoped
public class TokenProvider {
	
	private static final Map<String, Object> JWT_STATIC_CLAIMS = new HashMap<>();
	private static final Map<String, Object> JWT_STATIC_HEADERS = new HashMap<>();
	static {//TODO: AS JWS
		//https://www.eclipse.org/community/eclipse_newsletter/2017/september/article2.php
		JWT_STATIC_CLAIMS.put("iss", "https://server.example.com");
		JWT_STATIC_CLAIMS.put("sub", "Jessie");
		JWT_STATIC_CLAIMS.put("upn", "Jessie");
		JWT_STATIC_CLAIMS.put("aud", "targetService");
		JWT_STATIC_CLAIMS.put("groups", Arrays.asList("user", "protected"));
		
		
		JWT_STATIC_HEADERS.put("typ", "JWT");
		JWT_STATIC_HEADERS.put("alg", "RS256");
		JWT_STATIC_HEADERS.put("kid", "TODO");//TODO
	}
	
	private PrivateKey privateKey;

	@Inject
	@ConfigProperty(name="jwtKeyStorePath", defaultValue = "/yarp-jwt.keystore")
	private String keyStorePath;
	
	@Inject
	@ConfigProperty(name="jwtKeyStoreEntryName", defaultValue = "yarp jwt")
	private String jwtKeyStoreEntryName;
	
	@PostConstruct
	public void init() throws Exception {
		try {
			// TODO: passwort
			InputStream inputStream = TokenProvider.class.getResourceAsStream(keyStorePath);
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(inputStream, null);
			inputStream.close();

			KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(new char[0]);
			KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry("yarp jwt", protParam);
			privateKey = privateKeyEntry.getPrivateKey();
		} catch (Exception e) {
			throw new Exception("Could not load private key", e);
		} 
	}
	
	public String createToken(String userName) {
		// https://github.com/jwtk/jjwt
		// https://en.wikipedia.org/wiki/PKCS_8
		// https://github.com/MicroProfileJWT/eclipse-newsletter-sep-2017

		//TODO: username als sub!

        JwtBuilder builder = Jwts.builder()
        		.setHeader(JWT_STATIC_HEADERS)
        		.setClaims(JWT_STATIC_CLAIMS)
        		.setId(UUID.randomUUID().toString())
        		.setIssuedAt(new Date())
        		.setExpiration(new Date((System.currentTimeMillis() + 1000 * 60 * 60)));//TODO länge

        //TODO: log trace
//        System.out.println(builder.compact());
        
        return builder
        		.signWith(privateKey, SignatureAlgorithm.RS256)
        		.compact();
	}
	
	
	/**
	 * returns a JWT object, if the token is valid
	 * @param tokenString The plain JWT string
	 * @return A JWT object, if the token is valid. Otherwise null is returned
	 */
	public Jws<Claims> parseValidToken(String tokenString) {
		Jws<Claims> retVal = null; 
		try{
			retVal = Jwts.parser().setSigningKey(privateKey).parseClaimsJws(tokenString);
		}catch(Exception e) {
			//TODO: log trace
		}
		
		return retVal;
	}
}
