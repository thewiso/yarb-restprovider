package de.prettytree.yarb.restprovider.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import de.prettytree.yarb.restprovider.api.model.UserCredentials;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class SSLTest {

	@LocalServerPort
	private int port;

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	public void testWithoutSSLFailure() {
		String url = "http://localhost:" + port + contextPath + "/auth/login";
		UserCredentials userCredentials = TestUtils.getTestUserCredentials();

		ResponseEntity<String> response = restTemplate.postForEntity(url, userCredentials,
				String.class);
		
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Assertions.assertTrue(response.getBody().contains("This combination of host and port requires TLS."));
	}

}
