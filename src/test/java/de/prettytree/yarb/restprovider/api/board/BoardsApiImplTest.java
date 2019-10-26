package de.prettytree.yarb.restprovider.api.board;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.prettytree.yarb.restprovider.api.AuthApi;
import de.prettytree.yarb.restprovider.api.BoardsApi;
import de.prettytree.yarb.restprovider.api.model.Board;
import de.prettytree.yarb.restprovider.api.model.LoginData;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.test.TestUtils;

@RunWith(Arquillian.class)
@CleanupUsingScript(TestUtils.CLEANUP_DB_SCRIPT_PATH)
public class BoardsApiImplTest {

	@PersistenceContext
	private EntityManager em;

	private BoardsApi boardsApi;
	private AuthApi authApi;
	private ResteasyWebTarget target;

	@ArquillianResource
	private URL contextPath;

	@Deployment
	public static WebArchive createDeployment() {
		return TestUtils.createDefaultDeployment();
	}

	@Before
	public void init() {
		ResteasyClient client = TestUtils.createDefaultResteasyClient();
		target = client.target(contextPath + "yarb");

		authApi = target.proxy(AuthApi.class);
		boardsApi = target.proxy(BoardsApi.class);
	}

	@Test
	@UsingDataSet(TestUtils.DATA_SET_PATH)
	public void testGetBoardsSuccess() {
		UserCredentials credentials = TestUtils.getTestDataCredentials();
		LoginData loginData = authApi.login(credentials);

		BoardsApi authorisedBoardsApi = target.register(new ClientRequestFilter() {

			@Override
			public void filter(ClientRequestContext requestContext) throws IOException {
				requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer: " + loginData.getToken());
			}
		}).proxy(BoardsApi.class);

		List<Board> boards = authorisedBoardsApi.getBoards(loginData.getUser().getId());
		Assert.assertEquals(3, boards.size());
	}

	@Test
	public void testGetBoardsAuthorisationException() throws Throwable {
		TestUtils.assertThrowsException(() -> {
			boardsApi.getBoards(1);
		}, NotAuthorizedException.class);
	}

	@Test
	@UsingDataSet(TestUtils.DATA_SET_PATH)
	public void testGetBoardsForbiddenException() throws Throwable {
		AuthApi authApi = target.proxy(AuthApi.class);
		LoginData loginData = authApi.login(TestUtils.getTestDataCredentials());

		BoardsApi authorisedBoardsApi = target.register(new ClientRequestFilter() {

			@Override
			public void filter(ClientRequestContext requestContext) throws IOException {
				requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer: " + loginData.getToken());
			}
		}).proxy(BoardsApi.class);

		TestUtils.assertThrowsException(() -> {
			authorisedBoardsApi.getBoards(loginData.getUser().getId() + 1);
		}, ForbiddenException.class);
	}

}
