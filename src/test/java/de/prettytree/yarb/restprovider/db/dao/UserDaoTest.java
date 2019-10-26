package de.prettytree.yarb.restprovider.db.dao;

import java.util.Optional;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.test.TestUtils;

@RunWith(Arquillian.class)
@CleanupUsingScript(TestUtils.CLEANUP_DB_SCRIPT_PATH)
public class UserDaoTest {

	@Inject
	private UserDao dao;

	@Deployment
	public static WebArchive createDeployment() {
		return TestUtils.createDefaultDeployment();
	}

	@Test
	public void testFindByUsernameNoResult() {
		Optional<DB_User> userOpt = dao.findByUserName(TestUtils.getRandomString10());
		Assert.assertFalse(userOpt.isPresent());
	}
	
	@UsingDataSet(TestUtils.DATA_SET_PATH)
	@Test
	public void testFindByUsernameOneResult() {
		Optional<DB_User> userOpt = dao.findByUserName("mrfoo");
		Assert.assertTrue(userOpt.isPresent());
	}
	
}
