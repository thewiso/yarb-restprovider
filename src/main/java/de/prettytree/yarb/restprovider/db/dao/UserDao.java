package de.prettytree.yarb.restprovider.db.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.prettytree.yarb.restprovider.db.model.DB_User;

public interface UserDao extends JpaRepository<DB_User, Long> {

	Optional<DB_User> findByUserName(String username);

}
