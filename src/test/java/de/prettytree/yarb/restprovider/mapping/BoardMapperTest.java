package de.prettytree.yarb.restprovider.mapping;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.prettytree.yarb.restprovider.api.model.Board;
import de.prettytree.yarb.restprovider.db.model.DB_Board;
import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.test.TestUtils;

public class BoardMapperTest {

	@Test
	public void testBoardMapperFromDbToRestModel() {
		DB_User dbUser = new DB_User();
		dbUser.setId(Long.valueOf(TestUtils.getRandomInt()));
		
		DB_Board dbBoard = new DB_Board();
		dbBoard.setId(Long.valueOf(TestUtils.getRandomInt()));
		dbBoard.setName(TestUtils.getRandomString20());
		dbBoard.setCreatedAt(TestUtils.getRandomLocalDateTime());
		dbBoard.setOwner(dbUser);
		
		Board board = BoardMapper.map(dbBoard);
		
		Assertions.assertEquals(dbBoard.getId().longValue(), board.getId().longValue());
		Assertions.assertEquals(dbBoard.getName(), board.getName());
		Assertions.assertEquals(dbBoard.getCreatedAt(), board.getCreationDate().toLocalDateTime());
	}
}
