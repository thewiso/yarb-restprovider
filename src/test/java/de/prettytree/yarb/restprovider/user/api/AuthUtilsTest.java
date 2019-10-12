package de.prettytree.yarb.restprovider.user.api;

import javax.xml.bind.DatatypeConverter;

import org.junit.Assert;
import org.junit.Test;

import de.prettytree.yarb.restprovider.api.user.AuthUtils;
import de.prettytree.yarb.restprovider.api.user.HashException;

public class AuthUtilsTest {

	@Test
	public void testHashPasswordWithSalt() throws HashException {
		//select *,  encode("User".password, 'hex'), encode(salt, 'hex') from "User"
		String password = "Password1234";
		byte[] salt =  DatatypeConverter.parseHexBinary("a78a5934e354a12486036105360abc7fbf4d6cceaea0b204f3adbb38c8b785bc2af55474286b6391b9a783da1982da495197a96087f50bf1bc1dee0aed79913c890cde697101ce678f64161941281c439bcd0ee1fc622e2e06c2e6f9362967c22277b2fce1f4b1d2dc5c8b0d4fe333cfc273dc95fc2467639df45eb974d2bc34");
		byte[] hash = DatatypeConverter.parseHexBinary("3571b20bd66a427816974118ef8d0a2c72abc341b162c30ec787c0cc105d111812e5138a77be794c23709c3b1a4db91f85b09acd8cae62ecbe351c4ebbcee8680db4b47ea5f0b4dd9ce23a435e21ec7d04e7f93014ee712dbb0c1a6706c683788a425f0a4f8eb94e36fe7b40e10ac4f0ee892ee8d8e161e1a6ee5f13d10e892a");
		
		Assert.assertArrayEquals(AuthUtils.hashPasswordWithSalt(password, salt), hash);
	}
}
