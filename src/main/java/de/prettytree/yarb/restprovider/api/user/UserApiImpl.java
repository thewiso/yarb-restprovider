package de.prettytree.yarb.restprovider.api.user;

import java.security.SecureRandom;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.core.Response;

import de.prettytree.yarb.restprovider.db.model.User;
import de.prettytree.yarb.restprovider.user.api.UsersApi;
import de.prettytree.yarb.restprovider.user.model.PasswordWrapper;

@Stateless
public class UserApiImpl implements UsersApi {
	// TODO: prevent server from sending stacktrace
	@PersistenceContext
	private EntityManager em;

	private CriteriaBuilder criteriaBuilder;
	private static final SecureRandom random = new SecureRandom();

	@PostConstruct
	public void init() {
		criteriaBuilder = em.getCriteriaBuilder();
	}

	@Override
	public Response usersUsernamePut(@Pattern(regexp = "^[\\S]+$") @Size(min = 4, max = 20) String username,
			@Valid PasswordWrapper passwordWrapper) {
		username = username.toLowerCase();
		// https://en.wikibooks.org/wiki/Java_Persistence/Criteria
		CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
		Root<User> user = criteriaQuery.from(User.class);
		criteriaQuery.select(criteriaBuilder.count(user)).where(criteriaBuilder.equal(user.get("userName"), username));
		Query query = em.createQuery(criteriaQuery);

		Long result = (Long) query.getSingleResult();
		if (result == 0) {
			byte[] salt = new byte[AuthUtils.HASH_LENGTH];
			random.nextBytes(salt);

			try {
				byte[] hash = AuthUtils.hashPasswordWithSalt(passwordWrapper.getPassword(), salt);

				User newUser = new User();
				newUser.setPassword(hash);
				newUser.setSalt(salt);
				newUser.setUserName(username);

				em.persist(newUser);
			} catch (Throwable throwable) {
				// TODO Logging
				throwable.printStackTrace();
				return Response.status(500).build();
			}

			return Response.status(201).build();
		} else {
			// TODO: Responses as exceptions?
			return Response.status(409).entity("User already exists").build();
		}

	}
}
