package de.prettytree.yarb.restprovider.test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;

import de.prettytree.yarb.restprovider.api.model.UserCredentials;

public class TestUtils {

	public static LocalDateTime getRandomLocalDateTime() {
		long timeStamp = ThreadLocalRandom.current().nextLong(-System.currentTimeMillis(), System.currentTimeMillis());
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneId.systemDefault());
	}

	public static double getRandomDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}

	public static boolean getRandomBool() {
		return ThreadLocalRandom.current().nextBoolean();
	}

	public static String getRandomString10() {
		return RandomStringUtils.random(10);
	}

	public static String getRandomString20() {
		return RandomStringUtils.random(20);
	}

	public static String getRandomStringAlphabetic10() {
		return RandomStringUtils.randomAlphabetic(10);
	}

	public static byte[] getRandomByteArray() {
		return RandomStringUtils.random(20).getBytes(StandardCharsets.UTF_8);
	}

	public static long getRandomLong() {
		return ThreadLocalRandom.current().nextLong();
	}

	public static int getRandomInt() {
		return ThreadLocalRandom.current().nextInt();
	}

	@SuppressWarnings("unchecked")
	public static <ExceptionType extends Throwable> ExceptionType assertThrowsException(Runnable function,
			Class<ExceptionType> exceptionClass) throws AssertionError {
		Throwable throwable = null;

		try {
			function.run();
		} catch (Throwable e) {
			throwable = e;
		}

		if (throwable == null) {
			throw new AssertionError(String.format("Expected exception %s but got none", exceptionClass.getName()));
		}
		if (!throwable.getClass().equals(exceptionClass)) {
			throw new AssertionError(String.format("Expected exception %s but got %s", exceptionClass.getName(),
					throwable.getClass().getName()), throwable);
		}
		
		return (ExceptionType)throwable;
	}
	
	public static UserCredentials getMrFooCredentials() {
		UserCredentials retVal = new UserCredentials();
		retVal.setUsername("mrfoo");
		retVal.setPassword("foobar");
		return retVal;
	}
}
