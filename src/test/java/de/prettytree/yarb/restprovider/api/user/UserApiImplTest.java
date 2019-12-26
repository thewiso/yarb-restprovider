package de.prettytree.yarb.restprovider.api.user;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import de.prettytree.yarb.restprovider.api.UsersApi;
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
		UserCredentials userCredentials = TestUtils.getTestUserCredentials();
		UsersApi usersApi = testUtils.createClientApi(UsersApi.class, port, restTemplate, false);

		ResponseEntity<Void> response = usersApi.createUser(userCredentials);

		Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}

	@Test
	public void testCreateUserForDBEntryCreated() throws Throwable {
		String testUserName = TestUtils.getRandomStringAlphabetic10().toLowerCase();
		String testPassword = TestUtils.getRandomString20();

		UserCredentials userCredentials = new UserCredentials();
		userCredentials.setPassword(testPassword);
		userCredentials.setUsername(testUserName);
		
		UsersApi usersApi = testUtils.createClientApi(UsersApi.class, port, restTemplate, false);

		usersApi.createUser(userCredentials);

		DB_User dbUser = userDao.findByUserName(testUserName).get();
		byte[] hashedPassword = AuthUtils.hashPasswordWithSalt(testPassword, dbUser.getSalt());

		Assertions.assertTrue(Arrays.equals(hashedPassword, dbUser.getPassword()));
	}

	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	public void testGetUsersSuccess() {
		UserCredentials credentials = TestUtils.getTestUserCredentials();

		UsersApi usersApi = testUtils.createClientApi(UsersApi.class, port, restTemplate, true);
		User user = usersApi.getUser(TestUtils.TEST_USER_ID).getBody();

		Assertions.assertEquals(credentials.getUsername(), user.getUsername());
	}

	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	public void testGetUsersAuthorisationException() throws Throwable {
		UsersApi usersApi = testUtils.createClientApi(UsersApi.class, port, restTemplate, false);

		ResponseEntity<User> response = usersApi.getUser(-1);
		Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	public void testGetUsersForbiddenException() throws Throwable {
		UsersApi usersApi = testUtils.createClientApi(UsersApi.class, port, restTemplate, true);
		
		ResponseEntity<User> response = usersApi.getUser(TestUtils.TEST_USER_ID + 1);
		Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}

}
