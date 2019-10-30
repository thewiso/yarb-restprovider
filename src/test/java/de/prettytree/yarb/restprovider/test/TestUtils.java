package de.prettytree.yarb.restprovider.test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import de.prettytree.yarb.restprovider.api.model.UserCredentials;

@Component
public class TestUtils {

	public static final String TEST_DATA_PATH = "classpath:scripts/testdata.sql";

	public static final String LOGIN_PATH = "/auth/login";
	public static final String CREATE_USER_PATH = "/users";
	public static final String GET_USER_PATH = "/users/%d";

	@Value("${server.servlet.context-path}")
	private String contextPath;

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

		return (ExceptionType) throwable;
	}

	public static UserCredentials getTestDataCredentials() {
		UserCredentials retVal = new UserCredentials();
		retVal.setUsername("mrfoo");
		retVal.setPassword("foobar");
		return retVal;
	}

	public String getRestURL(int port, String path) {
		return "http://localhost:" + port + contextPath + path;
	}

	public static HttpHeaders createAuthBearerHeader(String token) {
		HttpHeaders retVal = new HttpHeaders();
		retVal.add("Authorization", "Bearer " + token);
		return retVal;
	}
}
