package de.prettytree.yarb.restprovider.api.user;

import java.security.SecureRandom;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import de.prettytree.yarb.restprovider.api.UsersApi;
import de.prettytree.yarb.restprovider.api.model.User;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.db.dao.UserDao;
import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.mapping.UserMapper;

@RestController
public class UsersApiImpl implements UsersApi {

	private static final Logger LOG = LoggerFactory.getLogger(UsersApiImpl.class);
	private static final SecureRandom random = new SecureRandom();

	private UserDao userDao;

	@Autowired
	public UsersApiImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public ResponseEntity<Void> createUser(@Valid UserCredentials userCredentials) {
		Optional<DB_User> existingUser = userDao.findByUserName(userCredentials.getUsername());
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
				LOG.error("Could not hash password", throwable);
				return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<User> getUser(Integer userId) {
		if (SecurityContextHolder.getContext().getAuthentication().getName().equals(userId.toString())) {
			Optional<DB_User> dbUser = userDao.findById(userId.longValue());
			if (dbUser.isPresent()) {
				return new ResponseEntity<User>(UserMapper.map(dbUser.get()), HttpStatus.OK);
			} else {
				LOG.error("Could not find user for AUTHORIZED user with id: " + userId);
				return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
			}
		}
		return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
	}

}
