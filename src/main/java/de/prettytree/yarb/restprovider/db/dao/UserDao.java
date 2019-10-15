package de.prettytree.yarb.restprovider.db.dao;

import java.util.Optional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.prettytree.yarb.restprovider.db.model.DB_User;

public class UserDao extends Dao<DB_User> {

	public UserDao() {
		super(DB_User.class);
	}

	//TODO: TEST
	public Optional<DB_User> get(String username) {
		CriteriaQuery<DB_User> criteriaQuery = criteriaBuilder.createQuery(DB_User.class);
		Root<DB_User> userRoot = criteriaQuery.from(DB_User.class);
		criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("userName"), username));
		TypedQuery<DB_User> query = em.createQuery(criteriaQuery);
		return getSingleResultOptional(query);
	}

}
