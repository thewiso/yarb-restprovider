package de.prettytree.yarb.restprovider.db.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BoardNote {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private String author;

	@Column(nullable = false)
	private long votes;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	@ManyToOne(optional = false)
	private BoardColumn boardColumn;

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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public long getVotes() {
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

	public BoardColumn getBoardColumn() {
		return boardColumn;
	}

	public void setBoardColumn(BoardColumn boardColumn) {
		this.boardColumn = boardColumn;
	}

}
