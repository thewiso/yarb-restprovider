package de.prettytree.yarb.restprovider.api.authentication;

import java.util.Arrays;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import de.prettytree.yarb.restprovider.api.AuthApi;
import de.prettytree.yarb.restprovider.api.infrastructure.security.TokenProvider;
import de.prettytree.yarb.restprovider.api.model.LoginData;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.api.user.AuthUtils;
import de.prettytree.yarb.restprovider.api.user.HashException;
import de.prettytree.yarb.restprovider.api.user.UsersApiImpl;
import de.prettytree.yarb.restprovider.db.dao.UserDao;
import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.mapping.UserMapper;

@RestController
public class AuthApiImpl implements AuthApi {

	private static final Logger LOG = LoggerFactory.getLogger(UsersApiImpl.class);
	private TokenProvider tokenProvider;
	private UserDao userDao;

	@Autowired
	public AuthApiImpl(TokenProvider tokenProvider, UserDao userDao) {
		this.tokenProvider = tokenProvider;
		this.userDao = userDao;
	}

	@Override
	public ResponseEntity<LoginData> login(@Valid UserCredentials userCredentials) {
		Optional<DB_User> user = userDao.findByUserName(userCredentials.getUsername());
		if (user.isPresent()) {
			byte[] hash = null;
			try {
				hash = AuthUtils.hashPasswordWithSalt(userCredentials.getPassword(), user.get().getSalt());
			} catch (HashException e) {
				LOG.error("Could not hash password", e);
				return new ResponseEntity<LoginData>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

			if (Arrays.equals(user.get().getPassword(), hash)) {
				LoginData retVal = new LoginData();
				retVal.setToken(tokenProvider.createToken(user.get().getId()));
				retVal.setUser(UserMapper.map(user.get()));
				return new ResponseEntity<LoginData>(retVal, HttpStatus.OK);
			}

		}
		return new ResponseEntity<LoginData>(HttpStatus.UNAUTHORIZED);
	}

	@Override
	public ResponseEntity<LoginData> refreshToken() {
		long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
		Optional<DB_User> user = userDao.findById(userId);
		
		LoginData retVal = new LoginData();
		retVal.setToken(tokenProvider.createToken(user.get().getId()));
		retVal.setUser(UserMapper.map(user.get()));
		return new ResponseEntity<LoginData>(retVal, HttpStatus.OK);
	}
}
