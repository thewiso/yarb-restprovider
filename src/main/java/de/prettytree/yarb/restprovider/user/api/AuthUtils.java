package de.prettytree.yarb.restprovider.user.api;

import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class AuthUtils {

	public static final int HASH_LENGTH = 128;
	private static final int ITERATION_COUNT = 10000; 
	
	//https://www.codeproject.com/Articles/704865/Salted-Password-Hashing-Doing-it-Right
	//https://csrc.nist.gov/publications/detail/sp/800-132/final
	public static byte[] hashPasswordWithSalt(String password, byte[] salt) throws HashException{
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, HASH_LENGTH * 8);
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			return factory.generateSecret(spec).getEncoded();
		} catch (Throwable throwable) {
			throw new HashException(throwable);
		}
		
	}
}
