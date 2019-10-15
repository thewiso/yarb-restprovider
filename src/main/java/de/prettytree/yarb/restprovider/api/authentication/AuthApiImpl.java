package de.prettytree.yarb.restprovider.api.authentication;

import java.util.Arrays;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import de.prettytree.yarb.restprovider.api.AuthApi;
import de.prettytree.yarb.restprovider.api.infrastructure.exceptionhandling.HttpResponseException;
import de.prettytree.yarb.restprovider.api.infrastructure.security.TokenProvider;
import de.prettytree.yarb.restprovider.api.model.LoginData;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.api.user.AuthUtils;
import de.prettytree.yarb.restprovider.api.user.HashException;
import de.prettytree.yarb.restprovider.db.dao.UserDao;
import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.mapping.UserMapper;

public class AuthApiImpl implements AuthApi {

	@Inject
	TokenProvider tokenProvider;

	@Inject
	UserMapper userMapper;
	
	@Inject 
	UserDao userDao;
	
	@Override
	public LoginData login(@Valid UserCredentials userCredentials) {
		Optional<DB_User> user = userDao.get(userCredentials.getUsername());
		// TODO: test!
		if (user.isPresent()) {
			byte[] hash = null;
			try {
				hash = AuthUtils.hashPasswordWithSalt(userCredentials.getPassword(), user.get().getSalt());
			} catch (HashException e) {
				throw new HttpResponseException(Response.Status.INTERNAL_SERVER_ERROR);
			}

			if (Arrays.equals(user.get().getPassword(), hash)) {
				LoginData retVal = new LoginData();
				retVal.setToken(tokenProvider.createToken(user.get().getId()));
				retVal.setUser(userMapper.map(user.get()));
				return retVal;
			}

		}
		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
	}

}
