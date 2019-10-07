package de.prettytree.yarb.restprovider.user.api;

import java.io.File;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

import de.prettytree.yarb.restprovider.user.db.User;
import de.prettytree.yarb.restprovider.user.model.PasswordWrapper;

@RunWith(Arquillian.class)
//@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserRestInterfaceTest {
	// TODO: auswechseln
	private static Logger log = Logger.getLogger(UserRestInterfaceTest.class.getName());
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
            .addPackages(true, "de.prettytree.accountAnalyzerRestProvider")
            .addAsResource("META-INF/persistence.test.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
            .addAsLibraries(files);
    }
	
	
	@Transactional
	@Test
	public void testCreateUserForExistingUser() throws Throwable {
		String testUserName = "testUser1";
		PasswordWrapper passwordWrapper = new PasswordWrapper();
		passwordWrapper.setPassword("Password1234");
			
		User user = new User();
		user.setPassword("1234".getBytes());
		user.setSalt("4321".getBytes());
		user.setUserName(testUserName);

		em.persist(user);
		Response response = userRestInterface.usersUsernamePut(testUserName, passwordWrapper);
		Assert.assertTrue("Wrong response code for creating existing user: " + response.getStatus(),
				response.getStatus() == 409);
	}
	
	@Test
	public void testCreateUserForNullParameter() throws Throwable {
		String testUserName = "testUser1";
		
		Response response = userRestInterface.usersUsernamePut(testUserName, null);
		System.out.println("STATUS: " + response.getStatus());
		Assert.assertTrue("Wrong response code for null parameter: " + response.getStatus(),
				response.getStatus() == 409);
	}
	
	
	@Test
	public void testCreateUserForDBEntryCreated() throws Throwable {
		String testUserName = "testUser2";
		
		PasswordWrapper passwordWrapper = new PasswordWrapper();
		passwordWrapper.setPassword("Password1234");
		
		userRestInterface.usersUsernamePut(testUserName, passwordWrapper);
		//TODO: check columns of new entry
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
		Root<User> user = criteriaQuery.from(User.class);
		criteriaQuery.select(criteriaBuilder.count(user)).where(criteriaBuilder.equal(user.get("userName"), testUserName));
		Query query = em.createQuery(criteriaQuery);

		Long result = (Long) query.getSingleResult();		
		
		Assert.assertTrue("User not in persisted in database",
				result == 1);
		
		
	}//TODO passwordlength
	
}
