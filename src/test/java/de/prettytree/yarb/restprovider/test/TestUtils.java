package de.prettytree.yarb.restprovider.test;

import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import de.prettytree.yarb.restprovider.api.AuthApi;
import de.prettytree.yarb.restprovider.api.model.LoginData;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;

@Component
public class TestUtils {

	public static final String TEST_DATA_PATH = "classpath:scripts/testdata.sql";
	private static final String LOCAL_HOST_URL = "https://localhost:";

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
	
	public static final int TEST_USER_ID = 10;
	public static UserCredentials getTestUserCredentials() {
		UserCredentials retVal = new UserCredentials();
		retVal.setUsername("mrfoo");
		retVal.setPassword("foobar");
		return retVal;
	}
	
	private HttpHeaders createTestUserAuthHeader(int port, TestRestTemplate restTemplate) {
		AuthApi authApi = createClientApi(AuthApi.class, port, restTemplate, false);
		LoginData loginData = authApi.login(getTestUserCredentials()).getBody();
		
		HttpHeaders retVal = new HttpHeaders();
		retVal.add("Authorization", "Bearer " + loginData.getToken());
		return retVal;
	}


	/**
	 * Creates a REST client implementation of the given interface
	 * @param <T> Type of the REST interface
	 * @param clazz Class of the REST interface
	 * @param port Port of the server
	 * @param restTemplate Template which is used for the service calls
	 * @param authenticate Whether to use a Authorization header of the test user ({@link getTestUserCredentials}) for the service calls
	 * @return A proxy object of the given interface
	 */
	@SuppressWarnings("unchecked")
	public <T> T createClientApi(Class<T> clazz, int port, TestRestTemplate restTemplate, boolean authenticate) {
		RestProxyInvocationHandler invocationHandler = null;

		if (authenticate) {
			HttpHeaders headers = createTestUserAuthHeader(port, restTemplate);
			invocationHandler = new RestProxyInvocationHandler(LOCAL_HOST_URL + port + contextPath, restTemplate,
					headers);
		} else {
			invocationHandler = new RestProxyInvocationHandler(LOCAL_HOST_URL + port + contextPath, restTemplate);
		}

		T proxyInstance = (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { clazz },
				invocationHandler);

		return proxyInstance;
	}
	
}
