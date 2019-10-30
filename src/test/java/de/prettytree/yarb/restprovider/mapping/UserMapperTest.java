package de.prettytree.yarb.restprovider.mapping;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.prettytree.yarb.restprovider.api.model.User;
import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.test.TestUtils;

public class UserMapperTest {
	
	@Test
	public void testUserMapperFromDbToRestModel() {
		DB_User dbUser = new DB_User();
		dbUser.setUserName(TestUtils.getRandomString20());
		dbUser.setId(Long.valueOf(TestUtils.getRandomInt()));
		
		User user = UserMapper.map(dbUser);
		
		Assertions.assertEquals(dbUser.getUserName(), user.getUsername());
		Assertions.assertEquals(dbUser.getId().longValue(), user.getId().longValue());
	}

}
