package de.prettytree.yarb.restprovider.mapping;

import de.prettytree.yarb.restprovider.api.model.User;
import de.prettytree.yarb.restprovider.db.model.DB_User;

public class UserMapper {

	public static User map(DB_User sourceModel) {
		User retVal = new User();

		retVal.setId(Math.toIntExact(sourceModel.getId()));
		retVal.setUsername(sourceModel.getUserName());

		return retVal;
	}

}
