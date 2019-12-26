package de.prettytree.yarb.restprovider.mapping;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.prettytree.yarb.restprovider.api.model.BoardNote;
import de.prettytree.yarb.restprovider.api.model.CreateBoardNote;
import de.prettytree.yarb.restprovider.db.model.DB_BoardColumn;
import de.prettytree.yarb.restprovider.db.model.DB_BoardNote;
import de.prettytree.yarb.restprovider.test.TestUtils;

public class BoardNoteMapperTest {

	@Test
	public void testMapFromDbToRestModel() {
		DB_BoardNote dbBoardNote = new DB_BoardNote();
		dbBoardNote.setId(Long.valueOf(TestUtils.getRandomInt()));
		dbBoardNote.setContent(TestUtils.getRandomString20());
		dbBoardNote.setCreatedAt(TestUtils.getRandomLocalDateTime());
		dbBoardNote.setVotes(TestUtils.getRandomInt());

		BoardNote boardNote = BoardNoteMapper.map(dbBoardNote);

		Assertions.assertEquals(dbBoardNote.getId(), boardNote.getId().longValue());
		Assertions.assertEquals(dbBoardNote.getContent(), boardNote.getContent());
		Assertions.assertEquals(dbBoardNote.getCreatedAt(), boardNote.getCreationDate().toLocalDateTime());
		Assertions.assertEquals(dbBoardNote.getVotes(), boardNote.getVotes().longValue());
	}

	@Test
	public void testMapFromRestToDbModel() {
		DB_BoardColumn dbColumn = new DB_BoardColumn();
		dbColumn.setId(Long.valueOf(TestUtils.getRandomInt()));

		CreateBoardNote createBoardNote = new CreateBoardNote();
		createBoardNote.setContent(TestUtils.getRandomString20());

		DB_BoardNote boardNote = BoardNoteMapper.map(createBoardNote, dbColumn);

		Assertions.assertEquals(0, boardNote.getVotes());
		Assertions.assertEquals(createBoardNote.getContent(), boardNote.getContent());
		Assertions.assertEquals(dbColumn, boardNote.getBoardColumn());
	}
}
