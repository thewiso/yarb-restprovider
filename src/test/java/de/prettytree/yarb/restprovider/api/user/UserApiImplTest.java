package de.prettytree.yarb.restprovider.api.user;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.ClientErrorException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.ArquillianProxyException;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.prettytree.yarb.restprovider.api.UsersApi;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.test.TestUtils;

@RunWith(Arquillian.class)
@Transactional
public class UserApiImplTest {
	// http://arquillian.org/guides/getting_started/?utm_source=cta

	@PersistenceContext(name = "FOO")
	private EntityManager em;

	private UsersApi usersApi;

	@ArquillianResource
    private URL contextPath;
	
	@Deployment
	public static WebArchive createDeployment() {
		File[] files = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeAndTestDependencies().resolve()
				.withTransitivity().asFile();

		return ShrinkWrap.create(WebArchive.class, UserApiImplTest.class.getSimpleName() + ".war")
				.addPackages(true, "de.prettytree.yarb.restprovider")
				.addAsResource("persistence.xml", "META-INF/persistence.xml")
				.addAsResource("yarb-jwt.keystore")
	            .addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"))
	            .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-web.xml"))
				.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
				.addAsLibraries(files);
	}

	@Before
	public void clearTestEntityTable() {
		TestUtils.truncateTable(em, DB_User.class);

		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(contextPath + "yarb");
		usersApi = target.proxy(UsersApi.class);
	}
	
	@Test
	public void testCreateUserForExistingUser() throws Throwable {
		String testUserName = "testuser1";
		UserCredentials userCredentials = new UserCredentials();
		userCredentials.setPassword("Password1234");
		userCredentials.setUsername(testUserName);

		DB_User user = new DB_User();
		user.setPassword("1234".getBytes());
		user.setSalt("4321".getBytes());
		user.setUserName(testUserName);

		em.persist(user);
		em.createQuery("commit").executeUpdate();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<DB_User> criteriaQuery = criteriaBuilder.createQuery(DB_User.class);
		Root<DB_User> userRoot = criteriaQuery.from(DB_User.class);
		criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("userName"), testUserName));
		TypedQuery<DB_User> query = em.createQuery(criteriaQuery);
		user = query.getSingleResult();
		
		ClientErrorException ex = null;
		try {
			usersApi.createUser(userCredentials);
		} catch (ArquillianProxyException e) {
			ex = (ClientErrorException) e.getCause();
		}
		
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

}
