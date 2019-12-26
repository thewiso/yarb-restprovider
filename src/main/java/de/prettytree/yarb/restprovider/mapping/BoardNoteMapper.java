package de.prettytree.yarb.restprovider.mapping;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import de.prettytree.yarb.restprovider.api.model.BoardNote;
import de.prettytree.yarb.restprovider.api.model.CreateBoardNote;
import de.prettytree.yarb.restprovider.db.model.DB_BoardColumn;
import de.prettytree.yarb.restprovider.db.model.DB_BoardNote;

public class BoardNoteMapper {
	public static DB_BoardNote map(CreateBoardNote boardNote, DB_BoardColumn column) {
		DB_BoardNote retVal = new DB_BoardNote();

		retVal.setBoardColumn(column);
		retVal.setContent(boardNote.getContent());
		retVal.setCreatedAt(LocalDateTime.now());

		return retVal;
	}

	public static BoardNote map(DB_BoardNote boardNote) {
		BoardNote retVal = new BoardNote();

		retVal.setId(boardNote.getId().intValue());
		retVal.setContent(boardNote.getContent());
		retVal.setCreationDate(OffsetDateTime.of(boardNote.getCreatedAt(), ZoneOffset.UTC));
		retVal.setVotes(boardNote.getVotes().intValue());

		return retVal;
	}
}
