package de.prettytree.yarb.restprovider.api.user;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

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
import de.prettytree.yarb.restprovider.api.UsersApi;
import de.prettytree.yarb.restprovider.api.model.LoginData;
import de.prettytree.yarb.restprovider.api.model.User;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.test.TestUtils;

@RunWith(Arquillian.class)
@CleanupUsingScript(TestUtils.CLEANUP_DB_SCRIPT_PATH)
public class UserApiImplTest {

	@PersistenceContext
	private EntityManager em;

	private UsersApi usersApi;
	private AuthApi authApi;
	private ResteasyWebTarget target;

	@ArquillianResource
	private URL contextPath;

	@Deployment
	public static WebArchive createDeployment() {
		return TestUtils.createDefaultDeployment();
	}

	@Before
	public void init() {
		ResteasyClient client = TestUtils.createDefaultResteasyClient();
		target = client.target(contextPath + "yarb");

		authApi = target.proxy(AuthApi.class);
		usersApi = target.proxy(UsersApi.class);
	}

	@UsingDataSet(TestUtils.DATA_SET_PATH)
	@Test
	public void testCreateUserForExistingUser() throws Throwable {
		ClientErrorException ex = TestUtils.assertThrowsException(() -> {
			usersApi.createUser(TestUtils.getTestDataCredentials());
		}, ClientErrorException.class);

		Assert.assertNotNull(ex);
		Assert.assertTrue("Wrong or no exception for creating existing user",
				ex != null && ex.getResponse().getStatus() == 409);
	}

	@Test
	public void testCreateUserForDBEntryCreated() throws Throwable {
		String testUserName = TestUtils.getRandomStringAlphabetic10().toLowerCase();
		String testPassword = TestUtils.getRandomString20();

		UserCredentials userCredentials = new UserCredentials();
		userCredentials.setPassword(testPassword);
		userCredentials.setUsername(testUserName);

		usersApi.createUser(userCredentials);

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<DB_User> criteriaQuery = criteriaBuilder.createQuery(DB_User.class);
		Root<DB_User> userRoot = criteriaQuery.from(DB_User.class);
		criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("userName"), testUserName));
		DB_User dbUser = em.createQuery(criteriaQuery).getSingleResult();

		byte[] hashedPassword = AuthUtils.hashPasswordWithSalt(testPassword, dbUser.getSalt());

		Assert.assertTrue("Password not persisted correctly", Arrays.equals(hashedPassword, dbUser.getPassword()));
	}

	@Test
	@UsingDataSet(TestUtils.DATA_SET_PATH)
	public void testGetUsersSuccess() {
		UserCredentials credentials = TestUtils.getTestDataCredentials();
		LoginData loginData = authApi.login(credentials);

		UsersApi authorizedUsersApi = target.register(new ClientRequestFilter() {

			@Override
			public void filter(ClientRequestContext requestContext) throws IOException {
				requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer: " + loginData.getToken());
			}
		}).proxy(UsersApi.class);

		User user = authorizedUsersApi.getUser(loginData.getUser().getId());
		Assert.assertEquals(credentials.getUsername(), user.getUsername());
	}

	@Test
	public void testGetUsersAuthorisationException() throws Throwable {
		TestUtils.assertThrowsException(() -> {
			usersApi.getUser(42);
		}, NotAuthorizedException.class);
	}

	@Test
	@UsingDataSet(TestUtils.DATA_SET_PATH)
	public void testGetUsersForbiddenException() throws Throwable {
		AuthApi authApi = target.proxy(AuthApi.class);
		LoginData loginData = authApi.login(TestUtils.getTestDataCredentials());

		UsersApi authorisedUsersApi = target.register(new ClientRequestFilter() {

			@Override
			public void filter(ClientRequestContext requestContext) throws IOException {
				requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer: " + loginData.getToken());
			}
		}).proxy(UsersApi.class);

		TestUtils.assertThrowsException(() -> {
			authorisedUsersApi.getUser(loginData.getUser().getId() + 1);
		}, ForbiddenException.class);
	}

}
