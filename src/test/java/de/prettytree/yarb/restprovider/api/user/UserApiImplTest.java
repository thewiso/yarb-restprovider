package de.prettytree.yarb.restprovider.api.user;

import java.io.File;
import java.util.Arrays;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prettytree.yarb.restprovider.api.infrastructure.exceptionhandling.HttpResponseException;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.api.user.AuthUtils;
import de.prettytree.yarb.restprovider.api.user.UserApiImpl;
import de.prettytree.yarb.restprovider.db.model.DB_User;

@RunWith(Arquillian.class)
public class UserApiImplTest {
	private static final Logger LOG = LoggerFactory.getLogger(UserApiImplTest.class);
	//http://arquillian.org/guides/getting_started/?utm_source=cta

	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private UserApiImpl userRestInterface;
	
	@Deployment
    public static WebArchive createDeployment() {
		File[] files = Maven.resolver().loadPomFromFile("pom.xml")
		        .importRuntimeDependencies().resolve().withTransitivity().asFile();
		
		return ShrinkWrap.create(WebArchive.class, "test.war")
            .addPackages(true, "de.prettytree.yarb.restprovider.api")
            .addPackages(true, "de.prettytree.yarb.restprovider.db")
            .addPackages(true, "de.prettytree.yarb.restprovider.mapping")
            .addAsResource("persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
            .addAsLibraries(files);
    }
	
	
	@Transactional
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
		
		WebApplicationException ex = null;
		try {
			userRestInterface.createUser(userCredentials);
		}catch(WebApplicationException e) {
			ex = e;
		}
		Assert.assertTrue("Wrong or no exception for creating existing user",
				ex != null && ex.getResponse().getStatus() == 409);
	}

	@Test
	public void testCreateUserForDBEntryCreated() throws Throwable {
		String testUserName = "testuser2";
		String testPassword = "Password1234";
		
		UserCredentials userCredentials = new UserCredentials();
		userCredentials.setPassword(testPassword);
		userCredentials.setUsername(testUserName);
		
		HttpResponseException exception = null;
		try {
			userRestInterface.createUser(userCredentials);
		}catch(HttpResponseException e) {
			exception = e;
		}
		
		Assert.assertEquals(exception.getResponse().getStatus(), Response.Status.CREATED.getStatusCode());
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<DB_User> criteriaQuery = criteriaBuilder.createQuery(DB_User.class);
		Root<DB_User> userRoot = criteriaQuery.from(DB_User.class);
		criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("userName"), testUserName));
		DB_User dbUser = em.createQuery(criteriaQuery).getSingleResult();
		
		byte[] hashedPassword = AuthUtils.hashPasswordWithSalt(testPassword, dbUser.getSalt());
		
		
		Assert.assertTrue("Password not persisted correctly",
				Arrays.equals(hashedPassword, dbUser.getPassword()));
	}
	
}
