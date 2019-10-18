package de.prettytree.yarb.restprovider.mapping;

import org.junit.Assert;
import org.junit.Test;

import de.prettytree.yarb.restprovider.api.model.User;
import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.test.TestUtils;

public class UserMapperTest {
	
	@Test
	public void testUserMapperFromDbToRestModel() {
		DB_User dbUser = new DB_User();
		dbUser.setUserName(TestUtils.getRandomString20());
		dbUser.setId(Long.valueOf(TestUtils.getRandomInt()));
		
		User user = new UserMapper().map(dbUser);
		
		Assert.assertEquals(dbUser.getUserName(), user.getUsername());
		Assert.assertEquals(dbUser.getId().longValue(), user.getId().longValue());
	}

}
