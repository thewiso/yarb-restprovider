package de.prettytree.yarb.restprovider.db.dao;

import java.io.File;
import java.util.Optional;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.test.TestUtils;

@RunWith(Arquillian.class)
@Transactional
public class UserDaoTest {

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

	@Test
	public void testFindByUsernameNoResult() {
		Optional<DB_User> userOpt = dao.findByUserName(TestUtils.getRandomString10());
		Assert.assertFalse(userOpt.isPresent());
	}
	
	@UsingDataSet("datasets/users_mrfoo.xml")
	@Test
	public void testFindByUsernameOneResult() {
		Optional<DB_User> userOpt = dao.findByUserName("mrfoo");
		Assert.assertTrue(userOpt.isPresent());
	}
	
}
