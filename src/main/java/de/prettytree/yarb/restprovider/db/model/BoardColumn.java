package de.prettytree.yarb.restprovider.db.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class BoardColumn {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne(optional = false)
	private Board board;

	@OneToMany(mappedBy = "boardColumn")
	private List<BoardNote> boardNotes = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public List<BoardNote> getBoardNotes() {
		return boardNotes;
	}

	public void setBoardNotes(List<BoardNote> boardNotes) {
		this.boardNotes = boardNotes;
	}

}
