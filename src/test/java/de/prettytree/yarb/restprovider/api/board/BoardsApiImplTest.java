package de.prettytree.yarb.restprovider.api.board;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode;
import org.springframework.transaction.annotation.Transactional;

import de.prettytree.yarb.restprovider.api.BoardsApi;
import de.prettytree.yarb.restprovider.api.model.Board;
import de.prettytree.yarb.restprovider.api.model.BoardColumn;
import de.prettytree.yarb.restprovider.api.model.CreateBoard;
import de.prettytree.yarb.restprovider.api.model.CreatedResponse;
import de.prettytree.yarb.restprovider.db.dao.BoardDao;
import de.prettytree.yarb.restprovider.db.model.DB_Board;
import de.prettytree.yarb.restprovider.test.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class BoardsApiImplTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestUtils testUtils;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	BoardDao boardDao;

	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	@Test
	public void testGetBoardsByOwnerSuccess() {
		BoardsApi boardsApi = testUtils.createClientApi(BoardsApi.class, port, restTemplate, true);

		List<Board> boards = boardsApi.getBoardsByOwner(TestUtils.TEST_USER_ID).getBody();
		Board board1 = boards.stream().filter(board -> board.getId().equals(20)).findAny().get();
		BoardColumn column1 = board1.getColumns().stream().filter(column -> column.getId().equals(30)).findAny().get();
		BoardColumn column2 = board1.getColumns().stream().filter(column -> column.getId().equals(31)).findAny().get();

		Assertions.assertEquals(3, boards.size());
		Assertions.assertEquals(4, board1.getColumns().size());
		Assertions.assertEquals(2, column1.getNotes().size());
		Assertions.assertEquals(1, column2.getNotes().size());
	}

	@Test
	public void testGetBoardsByOwnerUnauthorized() {
		BoardsApi boardsApi = testUtils.createClientApi(BoardsApi.class, port, restTemplate, false);
		Assertions.assertEquals(HttpStatus.UNAUTHORIZED,
				boardsApi.getBoardsByOwner(TestUtils.TEST_USER_ID).getStatusCode());
	}

	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	public void testGetBoardsByOwnerForbidden() {
		BoardsApi boardsApi = testUtils.createClientApi(BoardsApi.class, port, restTemplate, true);
		Assertions.assertEquals(HttpStatus.FORBIDDEN,
				boardsApi.getBoardsByOwner(TestUtils.TEST_USER_ID + 1).getStatusCode());
	}

	@Test
	public void testCreateBoardUnauthorized() {
		CreateBoard createBoard = BoardsApiImplTest.generateCreateBoard(3);
		BoardsApi boardsApi = testUtils.createClientApi(BoardsApi.class, port, restTemplate, false);
		Assertions.assertEquals(HttpStatus.UNAUTHORIZED, boardsApi.createBoard(createBoard).getStatusCode());
	}

	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH }, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED))
	@Transactional
	public void testCreateBoardSuccess() {
		CreateBoard createBoard = BoardsApiImplTest.generateCreateBoard(5);
		BoardsApi boardsApi = testUtils.createClientApi(BoardsApi.class, port, restTemplate, true);
		CreatedResponse response = boardsApi.createBoard(createBoard).getBody();

		DB_Board board = boardDao.findById(response.getId().longValue()).get();
		Assertions.assertEquals(createBoard.getName(), board.getName());
		Assertions.assertEquals(createBoard.getColumnNames().size(), board.getBoardColumns().size());

		List<String> actualColumnNames = board.getBoardColumns()
				.stream()
				.map(boardColumn -> boardColumn.getName())
				.collect(Collectors.toList());

		Assertions.assertEquals(createBoard.getColumnNames(), actualColumnNames);
	}

	private static CreateBoard generateCreateBoard(int columnCount) {
		CreateBoard retVal = new CreateBoard();
		retVal.setName(TestUtils.getRandomString10());

		for (int i = 0; i < columnCount; i++) {
			retVal.getColumnNames().add(TestUtils.getRandomString10());
		}

		return retVal;
	}

	@Test
	public void testGetBoardNotFound() {
		BoardsApi boardsApi = testUtils.createClientApi(BoardsApi.class, port, restTemplate, false);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, boardsApi.getBoard(-1).getStatusCode());
	}

	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	@Test
	public void testGetBoard() {
		BoardsApi boardsApi = testUtils.createClientApi(BoardsApi.class, port, restTemplate, false);

		Board board = boardsApi.getBoard(20).getBody();
		BoardColumn column1 = board.getColumns().stream().filter(column -> column.getId().equals(30)).findAny().get();
		BoardColumn column2 = board.getColumns().stream().filter(column -> column.getId().equals(31)).findAny().get();

		Assertions.assertEquals(4, board.getColumns().size());
		Assertions.assertEquals(2, column1.getNotes().size());
		Assertions.assertEquals(1, column2.getNotes().size());
	}
}
