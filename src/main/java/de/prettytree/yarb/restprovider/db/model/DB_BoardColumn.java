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
import javax.persistence.Table;

@Entity
@Table(name = "BOARD_COLUMN")
public class DB_BoardColumn {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne(optional = false)
	private DB_Board board;

	@OneToMany(mappedBy = "boardColumn")
	private List<DB_BoardNote> boardNotes = new ArrayList<>();

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

	public DB_Board getBoard() {
		return board;
	}

	public void setBoard(DB_Board board) {
		this.board = board;
	}

	public List<DB_BoardNote> getBoardNotes() {
		return boardNotes;
	}

	public void setBoardNotes(List<DB_BoardNote> boardNotes) {
		this.boardNotes = boardNotes;
	}

}
