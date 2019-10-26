package de.prettytree.yarb.restprovider.api.authentication;

import java.net.URL;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.prettytree.yarb.restprovider.api.AuthApi;
import de.prettytree.yarb.restprovider.api.model.User;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.test.TestUtils;

@RunWith(Arquillian.class)
@CleanupUsingScript(TestUtils.CLEANUP_DB_SCRIPT_PATH)
public class AuthApiImplTest {

	@Inject
	private AuthApi authApi;

	@ArquillianResource
	private URL contextPath;

	@Deployment
	public static WebArchive createDeployment() {
		return TestUtils.createDefaultDeployment();
	}

	@Before
	public void init() {
		ResteasyClient client = TestUtils.createDefaultResteasyClient();
		ResteasyWebTarget target = client.target(contextPath + "yarb");

		authApi = target.proxy(AuthApi.class);
	}

	@Test
	public void testLoginWithoutUser() {
		UserCredentials userCredentials = new UserCredentials();
		userCredentials.setPassword(TestUtils.getRandomString20());
		userCredentials.setUsername(TestUtils.getRandomStringAlphabetic10().toLowerCase());
		TestUtils.assertThrowsException(() -> {
			authApi.login(userCredentials);
		}, NotAuthorizedException.class);
	}

	@UsingDataSet(TestUtils.DATA_SET_PATH)
	@Test
	public void testLoginWithUser() throws Throwable {
		UserCredentials userCredentials = TestUtils.getTestDataCredentials();
		User user = authApi.login(userCredentials).getUser();

		Assert.assertEquals(1, user.getId().longValue());
		Assert.assertEquals("mrfoo", user.getUsername());
	}
}
