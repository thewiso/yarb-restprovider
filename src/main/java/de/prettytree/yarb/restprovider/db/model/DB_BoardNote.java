package de.prettytree.yarb.restprovider.db.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BOARD_NOTE")
public class DB_BoardNote {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private Long votes = 0L;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@ManyToOne(optional = false)
	private DB_BoardColumn boardColumn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getVotes() {
		return votes;
	}

	public void setVotes(long votes) {
		this.votes = votes;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public DB_BoardColumn getBoardColumn() {
		return boardColumn;
	}

	public void setBoardColumn(DB_BoardColumn boardColumn) {
		this.boardColumn = boardColumn;
	}

}
