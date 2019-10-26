package de.prettytree.yarb.restprovider.db.dao;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.prettytree.yarb.restprovider.db.model.DB_Board;
import de.prettytree.yarb.restprovider.test.TestUtils;

@RunWith(Arquillian.class)
@CleanupUsingScript(TestUtils.CLEANUP_DB_SCRIPT_PATH)
public class BoardDaoTest {

	@Inject
	private BoardDao dao;

	@Deployment
	public static WebArchive createDeployment() {
		return TestUtils.createDefaultDeployment();
	}

	@Test
	public void testFindByIdWithoutResult() {
		List<DB_Board> boardList = dao.findByUserId(1L);
		Assert.assertEquals(0, boardList.size());
	}

	@UsingDataSet(TestUtils.DATA_SET_PATH)
	@Test
	public void testFindByUsernameWithResult() {
		List<DB_Board> boardList = dao.findByUserId(1L);
		Assert.assertEquals(3, boardList.size());
	}
}
