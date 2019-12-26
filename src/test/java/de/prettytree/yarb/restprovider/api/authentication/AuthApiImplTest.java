package de.prettytree.yarb.restprovider.api.authentication;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import de.prettytree.yarb.restprovider.api.AuthApi;
import de.prettytree.yarb.restprovider.api.model.LoginData;
import de.prettytree.yarb.restprovider.api.model.User;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.test.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class AuthApiImplTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestUtils testUtils;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testLoginWithoutUser() {
		UserCredentials userCredentials = new UserCredentials();
		userCredentials.setPassword(TestUtils.getRandomString20());
		userCredentials.setUsername(TestUtils.getRandomStringAlphabetic10().toLowerCase());

		AuthApi authApi = testUtils.createClientApi(AuthApi.class, port, restTemplate, false);

		Assertions.assertEquals(HttpStatus.UNAUTHORIZED, authApi.login(userCredentials).getStatusCode());
	}

	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	@Test
	public void testLoginWithUser() throws Throwable {
		UserCredentials userCredentials = TestUtils.getTestUserCredentials();
		AuthApi authApi = testUtils.createClientApi(AuthApi.class, port, restTemplate, false);

		User user = authApi.login(userCredentials).getBody().getUser();

		Assertions.assertEquals(TestUtils.TEST_USER_ID, user.getId());
		Assertions.assertEquals("mrfoo", user.getUsername());
	}

	@Test
	public void testRefreshTokenWithoutJWT() {
		AuthApi authApi = testUtils.createClientApi(AuthApi.class, port, restTemplate, false);
		Assertions.assertEquals(HttpStatus.UNAUTHORIZED, authApi.refreshToken().getStatusCode());
	}

	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	@Test
	public void testRefreshTokenWithJWT() throws Throwable {
		AuthApi authApi = testUtils.createClientApi(AuthApi.class, port, restTemplate, false);

		UserCredentials credentials = TestUtils.getTestUserCredentials();
		LoginData loginData = authApi.login(credentials).getBody();

		authApi = testUtils.createClientApi(AuthApi.class, port, restTemplate, true);
		LoginData newLoginData = authApi.refreshToken().getBody();

		Assertions.assertEquals(loginData.getUser().getId(), newLoginData.getUser().getId());
	}
}
