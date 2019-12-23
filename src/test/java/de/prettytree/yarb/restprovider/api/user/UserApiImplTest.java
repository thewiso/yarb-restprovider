package de.prettytree.yarb.restprovider.api.user;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import de.prettytree.yarb.restprovider.api.model.LoginData;
import de.prettytree.yarb.restprovider.api.model.User;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.db.dao.UserDao;
import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.test.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserApiImplTest {

	@Autowired
	UserDao userDao;

	@LocalServerPort
	private int port;

	@Autowired
	private TestUtils testUtils;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	public void testCreateUserForExistingUser() throws Throwable {
		UserCredentials userCredentials = TestUtils.getTestDataCredentials();

		ResponseEntity<String> response = restTemplate
				.postForEntity(testUtils.getRestURL(port, TestUtils.CREATE_USER_PATH), userCredentials, String.class);

		Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}

	@Test
	public void testCreateUserForDBEntryCreated() throws Throwable {
		String testUserName = TestUtils.getRandomStringAlphabetic10().toLowerCase();
		String testPassword = TestUtils.getRandomString20();

		UserCredentials userCredentials = new UserCredentials();
		userCredentials.setPassword(testPassword);
		userCredentials.setUsername(testUserName);

		restTemplate.postForEntity(testUtils.getRestURL(port, TestUtils.CREATE_USER_PATH), userCredentials,
				String.class);

		DB_User dbUser = userDao.findByUserName(testUserName).get();
		byte[] hashedPassword = AuthUtils.hashPasswordWithSalt(testPassword, dbUser.getSalt());

		Assertions.assertTrue(Arrays.equals(hashedPassword, dbUser.getPassword()));
	}

	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	public void testGetUsersSuccess() {
		UserCredentials credentials = TestUtils.getTestDataCredentials();
		LoginData loginData = restTemplate
				.postForEntity(testUtils.getRestURL(port, TestUtils.LOGIN_PATH), credentials, LoginData.class)
				.getBody();

		HttpEntity<HttpHeaders> authHeader = new HttpEntity<>(TestUtils.createAuthBearerHeader(loginData.getToken()));

		User user = restTemplate
				.exchange(
						testUtils.getRestURL(port, String.format(TestUtils.GET_USER_PATH, loginData.getUser().getId())),
						HttpMethod.GET, authHeader, User.class)
				.getBody();

		Assertions.assertEquals(credentials.getUsername(), user.getUsername());
	}

	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	public void testGetUsersAuthorisationException() throws Throwable {
		ResponseEntity<User> response = restTemplate
				.getForEntity(testUtils.getRestURL(port, String.format(TestUtils.GET_USER_PATH, 42)), User.class);
		Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	public void testGetUsersForbiddenException() throws Throwable {
		UserCredentials credentials = TestUtils.getTestDataCredentials();
		LoginData loginData = restTemplate
				.postForEntity(testUtils.getRestURL(port, TestUtils.LOGIN_PATH), credentials, LoginData.class)
				.getBody();

		HttpEntity<HttpHeaders> authHeader = new HttpEntity<>(TestUtils.createAuthBearerHeader(loginData.getToken()));
		ResponseEntity<User> response = restTemplate.exchange(
				testUtils.getRestURL(port, String.format(TestUtils.GET_USER_PATH, loginData.getUser().getId() + 1)),
				HttpMethod.GET, authHeader, User.class);

		Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}

}
