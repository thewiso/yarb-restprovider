package de.prettytree.yarb.restprovider.api.authentication;

import java.io.File;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.prettytree.yarb.restprovider.api.model.User;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.api.user.AuthUtils;
import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.test.TestUtils;

@RunWith(Arquillian.class)
@Transactional
public class AuthApiImplTest {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private AuthApiImpl authApi;

	@Deployment
	public static WebArchive createDeployment() {
		File[] files = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeAndTestDependencies().resolve()
				.withTransitivity().asFile();

		return ShrinkWrap.create(WebArchive.class, AuthApiImplTest.class.getSimpleName() + ".war")
				.addPackages(true, "de.prettytree.yarb.restprovider")
				.addAsResource("yarb-jwt.keystore")
				.addAsResource("persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
				.addAsLibraries(files);
	}

	@Before
	public void clearTestEntityTable() {
		TestUtils.truncateTable(em, DB_User.class);
	}

	@Test()
	public void testLoginWithoutUser() {
		WebApplicationException exception = null;
		try {
			UserCredentials userCredentials = new UserCredentials();
			userCredentials.setPassword(TestUtils.getRandomString20());
			userCredentials.setUsername(TestUtils.getRandomStringAlphabetic10().toLowerCase());
			authApi.login(userCredentials);
		} catch (WebApplicationException e) {
			exception = e;
		}
		
		Assert.assertNotNull(exception);
		Assert.assertEquals(exception.getResponse().getStatus(), Response.Status.UNAUTHORIZED.getStatusCode());
	}
	
	@Test()
	public void testLoginWithUser() throws Throwable {
		String userName = TestUtils.getRandomString10();
		String password = TestUtils.getRandomString20();
		byte[] salt = TestUtils.getRandomByteArray();
		byte[] hashedPassword = AuthUtils.hashPasswordWithSalt(password, salt);
		
		DB_User dbUser = new DB_User();
		dbUser.setUserName(userName);
		dbUser.setPassword(hashedPassword);
		dbUser.setSalt(salt);
		em.persist(dbUser);
		
		UserCredentials userCredentials = new UserCredentials();
		userCredentials.setPassword(password);
		userCredentials.setUsername(userName);
		User user = authApi.login(userCredentials).getUser();
						
		Assert.assertEquals(dbUser.getId().longValue(), user.getId().longValue());
		Assert.assertEquals(dbUser.getUserName(), user.getUsername());
	}
}
