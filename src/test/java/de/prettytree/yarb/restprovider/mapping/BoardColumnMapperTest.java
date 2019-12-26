package de.prettytree.yarb.restprovider.mapping;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.prettytree.yarb.restprovider.api.model.BoardColumn;
import de.prettytree.yarb.restprovider.db.model.DB_Board;
import de.prettytree.yarb.restprovider.db.model.DB_BoardColumn;
import de.prettytree.yarb.restprovider.db.model.DB_BoardNote;
import de.prettytree.yarb.restprovider.test.TestUtils;

public class BoardColumnMapperTest {

	@Test
	public void testMapFromDbToRestModel() {
		DB_BoardColumn dbBoardColumn = new DB_BoardColumn();
		dbBoardColumn.setId(Long.valueOf(TestUtils.getRandomInt()));
		dbBoardColumn.setName(TestUtils.getRandomString20());

		int noteCount = 12;
		for (int i = 0; i < noteCount; i++) {
			DB_BoardNote dbBoardNote = new DB_BoardNote();
			dbBoardNote.setId(Long.valueOf(i));
			dbBoardNote.setVotes(0);
			dbBoardNote.setCreatedAt(LocalDateTime.now());
			dbBoardColumn.getBoardNotes().add(dbBoardNote);
		}

		BoardColumn boardColumn = BoardColumnMapper.map(dbBoardColumn);

		Assertions.assertEquals(dbBoardColumn.getId().intValue(), boardColumn.getId());
		Assertions.assertEquals(dbBoardColumn.getName(), boardColumn.getName());
		Assertions.assertEquals(noteCount, boardColumn.getNotes().size());
	}
	
	@Test
	public void testMapFromRestToDbModel() {
		DB_Board dBoard = new DB_Board();
		dBoard.setId(Long.valueOf(TestUtils.getRandomInt()));
		
		String boardName = TestUtils.getRandomString20();

		DB_BoardColumn boardColumn = BoardColumnMapper.map(boardName, dBoard);

		Assertions.assertEquals(boardName, boardColumn.getName());
		Assertions.assertEquals(dBoard, boardColumn.getBoard());
	}
}
