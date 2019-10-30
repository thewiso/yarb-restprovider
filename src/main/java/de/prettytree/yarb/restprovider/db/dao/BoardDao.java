package de.prettytree.yarb.restprovider.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.prettytree.yarb.restprovider.db.model.DB_Board;

public interface BoardDao extends JpaRepository<DB_Board, Long> {

	List<DB_Board> findByOwner(long owner);
}
