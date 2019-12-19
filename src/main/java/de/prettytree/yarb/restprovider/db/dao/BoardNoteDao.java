package de.prettytree.yarb.restprovider.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import de.prettytree.yarb.restprovider.db.model.DB_BoardNote;

public interface BoardNoteDao extends JpaRepository<DB_BoardNote, Long> {

}
