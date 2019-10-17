package de.prettytree.yarb.restprovider.api.user;

import java.security.SecureRandom;
import java.util.Optional;

import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.validation.Valid;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prettytree.yarb.restprovider.api.UsersApi;
import de.prettytree.yarb.restprovider.api.infrastructure.exceptionhandling.HttpResponseException;
import de.prettytree.yarb.restprovider.api.model.User;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.db.dao.UserDao;
import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.mapping.UserMapper;

public class UserApiImpl implements UsersApi {

	private static final Logger LOG = LoggerFactory.getLogger(UserApiImpl.class);
	private static final SecureRandom random = new SecureRandom();

	@Inject
	UserDao userDao;

	@Inject
	UserMapper userMapper;
	
	@Inject
	private SecurityContext securityContext;

	@Override
	public void createUser(@Valid UserCredentials userCredentials) {
		Optional<DB_User> existingUser = userDao.get(userCredentials.getUsername());
		if (!existingUser.isPresent()) {
			byte[] salt = new byte[AuthUtils.HASH_LENGTH];
			random.nextBytes(salt);

			try {
				byte[] hash = AuthUtils.hashPasswordWithSalt(userCredentials.getPassword(), salt);

				DB_User newUser = new DB_User();
				newUser.setPassword(hash);
				newUser.setSalt(salt);
				newUser.setUserName(userCredentials.getUsername());

				userDao.save(newUser);
			} catch (Throwable throwable) {
				throw new HttpResponseException(throwable);
			}
			throw new HttpResponseException(Response.Status.CREATED);
		} else {
			throw new HttpResponseException(Response.Status.CONFLICT);

		}

	}

	@Override
	public User getUser(Integer userId) {
		if(securityContext.getCallerPrincipal().getName().equals(userId.toString())) {
			Optional<DB_User> dbUser = userDao.find(userId);
			if(dbUser.isPresent()) {
				return userMapper.map(dbUser.get());
			}else {
				LOG.error("Could not find user for AUTHORIZED user with id: " + userId);
				throw new HttpResponseException(Response.Status.NOT_FOUND);
			}
		}
		throw new HttpResponseException(Response.Status.FORBIDDEN);
	}

}
