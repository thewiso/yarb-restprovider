package de.prettytree.yarb.restprovider.mapping;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.prettytree.yarb.restprovider.api.model.Board;
import de.prettytree.yarb.restprovider.api.model.CreateBoard;
import de.prettytree.yarb.restprovider.db.model.DB_Board;
import de.prettytree.yarb.restprovider.db.model.DB_BoardColumn;
import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.test.TestUtils;

public class BoardMapperTest {

	@Test
	public void testMapFromDbToRestModel() {
		DB_User dbUser = new DB_User();
		dbUser.setId(Long.valueOf(TestUtils.getRandomInt()));

		DB_Board dbBoard = new DB_Board();
		dbBoard.setId(Long.valueOf(TestUtils.getRandomInt()));
		dbBoard.setName(TestUtils.getRandomString20());
		dbBoard.setCreatedAt(TestUtils.getRandomLocalDateTime());
		dbBoard.setOwner(dbUser);

		int columnCount = 3;
		for (int i = 0; i < 3; i++) {
			DB_BoardColumn column = new DB_BoardColumn();
			column.setId(Long.valueOf(i));
			dbBoard.getBoardColumns().add(column);
		}

		Board board = BoardMapper.map(dbBoard);

		Assertions.assertEquals(dbBoard.getId().longValue(), board.getId().longValue());
		Assertions.assertEquals(dbBoard.getName(), board.getName());
		Assertions.assertEquals(dbBoard.getCreatedAt(), board.getCreationDate().toLocalDateTime());
		Assertions.assertEquals(columnCount, dbBoard.getBoardColumns().size());
	}

	@Test
	public void testMapFromRestToDbModel() {
		DB_User dbUser = new DB_User();
		dbUser.setId(Long.valueOf(TestUtils.getRandomInt()));

		CreateBoard createBoard = new CreateBoard();
		createBoard.setName(TestUtils.getRandomString20());

		int columnCount = 4;
		for (int i = 0; i < columnCount; i++) {
			createBoard.getColumnNames().add(TestUtils.getRandomString20());
		}

		DB_Board board = BoardMapper.map(createBoard, dbUser);
		List<String> actualColumnNames = board.getBoardColumns()
				.stream()
				.map(boardColumn -> boardColumn.getName())
				.collect(Collectors.toList());

		Assertions.assertEquals(createBoard.getName(), board.getName());
		Assertions.assertEquals(dbUser, board.getOwner());
		Assertions.assertEquals(createBoard.getColumnNames(), actualColumnNames);
	}
}
