package de.prettytree.yarb.restprovider.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import de.prettytree.yarb.restprovider.db.model.DB_BoardColumn;

public interface BoardColumnDao extends JpaRepository<DB_BoardColumn, Long> {

}
