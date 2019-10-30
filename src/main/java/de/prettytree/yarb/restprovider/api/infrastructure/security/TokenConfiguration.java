package de.prettytree.yarb.restprovider.api.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:yarb.properties")
@ConfigurationProperties(prefix = "de.prettytree.yarb.restprovider")
public class TokenConfiguration {

	private String keyStorePath;
	private String keyStoreEntryName;
	private String keyStorePassword;
	private Long tokenExpirationTimeMillis;

	public String getKeyStorePath() {
		return keyStorePath;
	}

	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}

	public String getKeyStoreEntryName() {
		return keyStoreEntryName;
	}

	public void setKeyStoreEntryName(String keyStoreEntryName) {
		this.keyStoreEntryName = keyStoreEntryName;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	public Long getTokenExpirationTimeMillis() {
		return tokenExpirationTimeMillis;
	}

	public void setTokenExpirationTimeMillis(Long tokenExpirationTimeMillis) {
		this.tokenExpirationTimeMillis = tokenExpirationTimeMillis;
	}
}
