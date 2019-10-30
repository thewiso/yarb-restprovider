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

		Assertions.assertEquals(HttpStatus.UNAUTHORIZED,
				restTemplate
						.postForEntity(testUtils.getRestURL(port, TestUtils.LOGIN_PATH), userCredentials,
								LoginData.class)
						.getStatusCode());
	}

	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	@Test
	public void testLoginWithUser() throws Throwable {
		UserCredentials userCredentials = TestUtils.getTestDataCredentials();
		User user = restTemplate
				.postForEntity(testUtils.getRestURL(port, TestUtils.LOGIN_PATH), userCredentials, LoginData.class)
				.getBody()
				.getUser();

		Assertions.assertEquals(1, user.getId().longValue());
		Assertions.assertEquals("mrfoo", user.getUsername());
	}
}
