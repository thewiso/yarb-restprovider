package de.prettytree.yarb.restprovider.api.board;

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
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import de.prettytree.yarb.restprovider.api.BoardsApi;
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

//	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
//	@Test
//	public void testGetBoardsByOwnerSuccess() {
//		UserCredentials credentials = TestUtils.getTestDataCredentials();
//		LoginData loginData = authApi.login(credentials);
//
//		BoardsApi authorisedBoardsApi = target.register(new ClientRequestFilter() {
//
//			@Override
//			public void filter(ClientRequestContext requestContext) throws IOException {
//				requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer: " + loginData.getToken());
//			}
//		}).proxy(BoardsApi.class);
//
//		List<Board> boards = authorisedBoardsApi.getBoards(loginData.getUser().getId());
//		Assert.assertEquals(3, boards.size());
//	}

	@Test
	public void testGetBoardsByOwnerAuthorisationException() throws Throwable {
		BoardsApi boardsApi = testUtils.createClientApi(BoardsApi.class, port, restTemplate, false);
		Assertions.assertEquals(HttpStatus.UNAUTHORIZED, boardsApi.getBoardsByOwner(TestUtils.TEST_USER_ID).getStatusCode());
	}

//	@Test
//	@UsingDataSet(TestUtils.DATA_SET_PATH)
//	public void testGetBoardsByOwnerForbiddenException() throws Throwable {
//		AuthApi authApi = target.proxy(AuthApi.class);
//		LoginData loginData = authApi.login(TestUtils.getTestDataCredentials());
//
//		BoardsApi authorisedBoardsApi = target.register(new ClientRequestFilter() {
//
//			@Override
//			public void filter(ClientRequestContext requestContext) throws IOException {
//				requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer: " + loginData.getToken());
//			}
//		}).proxy(BoardsApi.class);
//
//		TestUtils.assertThrowsException(() -> {
//			authorisedBoardsApi.getBoards(loginData.getUser().getId() + 1);
//		}, ForbiddenException.class);
//	}
//	
//	private static String createGetBoardsByOwnerUrl(long userId) {
//		return String.format(TestUtils.GET_BOARDS_BY_OWNER_PATH, userId);
//	}

}
//TODO