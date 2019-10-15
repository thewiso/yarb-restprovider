package de.prettytree.yarb.restprovider.db.model;

import java.time.LocalDateTime;
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
@Table(name = "BOARD")
public class DB_Board {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne(optional = false)
	private DB_User owner;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "board")
	private List<DB_BoardColumn> boardColumns = new ArrayList<>();

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

	public DB_User getOwner() {
		return owner;
	}

	public void setOwner(DB_User owner) {
		this.owner = owner;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<DB_BoardColumn> getBoardColumns() {
		return boardColumns;
	}

	public void setBoardColumns(List<DB_BoardColumn> boardColumns) {
		this.boardColumns = boardColumns;
	}

}
