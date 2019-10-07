package de.prettytree.yarb.restprovider.authentication.api;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.ws.rs.core.Response;

import de.prettytree.yarb.restprovider.authentication.model.LoginCredentials;
import de.prettytree.yarb.restprovider.infrastructure.security.TokenProvider;
import de.prettytree.yarb.restprovider.user.api.AuthUtils;
import de.prettytree.yarb.restprovider.user.api.HashException;
import de.prettytree.yarb.restprovider.user.db.User;

@Stateless
public class TokenApiImpl implements TokenApi{

	@Inject
	TokenProvider tokenProvider;
	
	@PersistenceContext
	private EntityManager em;

	private CriteriaBuilder criteriaBuilder;
	
	@PostConstruct
	public void init() {
		criteriaBuilder = em.getCriteriaBuilder();
	}
	
	
	@Override
	public Response tokenCreatePost(@Valid LoginCredentials loginCredentials) {
		String username = loginCredentials.getUsername().toLowerCase();
		
		CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
		Root<User> userRoot = criteriaQuery.from(User.class);
		criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("userName"), username));
		Query query = em.createQuery(criteriaQuery);
		User user = (User) query.getSingleResult();
		
		if(user != null) {
			byte[] hash = null;
			try {
				hash = AuthUtils.hashPasswordWithSalt(loginCredentials.getPassword(), user.getSalt());
			} catch (HashException e) {
				return Response.status(500).build();
			}
			
			if(Arrays.equals(user.getPassword(), hash)) {
				return Response.ok(tokenProvider.createToken(loginCredentials.getUsername().toLowerCase())).build();
			}
			
		}
		return Response.status(401).build();
		
	}

}
