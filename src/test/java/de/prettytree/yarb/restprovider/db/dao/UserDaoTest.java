package de.prettytree.yarb.restprovider.db.dao;

import java.io.File;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.test.TestUtils;

@RunWith(Arquillian.class)
@Transactional
public class UserDaoTest {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private UserDao dao;

	@Deployment
	public static WebArchive createDeployment() {
		File[] files = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeAndTestDependencies().resolve()
				.withTransitivity().asFile();

		return ShrinkWrap.create(WebArchive.class, UserDaoTest.class.getSimpleName() + ".war")
				.addPackages(true, "de.prettytree.yarb.restprovider.db")
				.addPackages(true, "de.prettytree.yarb.restprovider.test")
				.addAsResource("persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml")).addAsLibraries(files);
	}

	@Before
	public void clearUserTable() {
		TestUtils.truncateTable(em, DB_User.class);
	}
	
	@Test
	public void testFindByUsernameNoResult() {
		Optional<DB_User> userOpt = dao.findByUserName(TestUtils.getRandomString10());
		Assert.assertFalse(userOpt.isPresent());
	}
	
	@Test
	public void testFindByUsernameOneResult() {
		DB_User user = new DB_User();
		user.setPassword(TestUtils.getRandomByteArray());
		user.setSalt(TestUtils.getRandomByteArray());
		user.setUserName(TestUtils.getRandomString10());
		em.persist(user);
		
		Optional<DB_User> userOpt = dao.findByUserName(user.getUserName());
		Assert.assertTrue(userOpt.isPresent());
	}
	
}
