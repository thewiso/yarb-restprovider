package de.prettytree.yarb.restprovider.api.notes;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import de.prettytree.yarb.restprovider.api.NotesApi;
import de.prettytree.yarb.restprovider.api.model.CreateBoardNote;
import de.prettytree.yarb.restprovider.api.model.CreatedResponse;
import de.prettytree.yarb.restprovider.api.model.UpdateBoardNote;
import de.prettytree.yarb.restprovider.db.dao.BoardNoteDao;
import de.prettytree.yarb.restprovider.db.model.DB_BoardNote;
import de.prettytree.yarb.restprovider.test.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class NotesApiImplTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestUtils testUtils;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private BoardNoteDao noteDao;

	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	@Test
	public void testCreateNoteSuccess() {
		Long columnId = 30L;

		NotesApi notesApi = testUtils.createClientApi(NotesApi.class, port, restTemplate, false);

		CreateBoardNote createBoardNote = new CreateBoardNote();
		createBoardNote.setContent(TestUtils.getRandomString20());
		createBoardNote.setBoardColumnId(columnId.intValue());

		CreatedResponse response = notesApi.createNote(createBoardNote).getBody();

		DB_BoardNote dbNote = noteDao.findById(response.getId().longValue()).get();

		Assertions.assertEquals(createBoardNote.getContent(), dbNote.getContent());
		Assertions.assertEquals(columnId, dbNote.getBoardColumn().getId());
		Assertions.assertEquals(0, dbNote.getVotes());
	}

	@Test
	public void testCreateNoteNotFound() {
		NotesApi notesApi = testUtils.createClientApi(NotesApi.class, port, restTemplate, false);

		CreateBoardNote createBoardNote = new CreateBoardNote();
		createBoardNote.setContent(TestUtils.getRandomString20());
		createBoardNote.setBoardColumnId(-1);

		Assertions.assertEquals(HttpStatus.NOT_FOUND, notesApi.createNote(createBoardNote).getStatusCode());
	}

	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	@Test
	public void testDeleteNoteSuccess() {
		Long noteId = 40L;
		Optional<DB_BoardNote> dbNote = noteDao.findById(noteId);
		Assertions.assertEquals(true, dbNote.isPresent());

		NotesApi notesApi = testUtils.createClientApi(NotesApi.class, port, restTemplate, false);

		ResponseEntity<Void> response = notesApi.deleteNote(noteId.intValue());
		dbNote = noteDao.findById(noteId);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(false, dbNote.isPresent());
	}

	@Test
	public void testDeleteNoteNotFound() {
		NotesApi notesApi = testUtils.createClientApi(NotesApi.class, port, restTemplate, false);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, notesApi.deleteNote(-1).getStatusCode());
	}

	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	public void testUpdateNoteSuccess() {
		Long noteId = 40L;
		Long votes = 3L;
		NotesApi notesApi = testUtils.createClientApi(NotesApi.class, port, restTemplate, false);

		UpdateBoardNote updateBoardNote = new UpdateBoardNote();
		updateBoardNote.setContent(TestUtils.getRandomString20());

		ResponseEntity<Void> response = notesApi.updateNote(noteId.intValue(), updateBoardNote);
		DB_BoardNote dbNote = noteDao.findById(noteId).get();

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(updateBoardNote.getContent(), dbNote.getContent());
		Assertions.assertEquals(votes, dbNote.getVotes());
	}

	@Test
	public void testUpdateNoteNotFound() {
		NotesApi notesApi = testUtils.createClientApi(NotesApi.class, port, restTemplate, false);

		UpdateBoardNote updateBoardNote = new UpdateBoardNote();
		updateBoardNote.setContent(TestUtils.getRandomString20());

		Assertions.assertEquals(HttpStatus.NOT_FOUND, notesApi.updateNote(-1, updateBoardNote).getStatusCode());
	}
	
	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	public void testDeleteVoteSuccess() {
		Long noteId = 42L;
		Long originalVotes = 2L;
		NotesApi notesApi = testUtils.createClientApi(NotesApi.class, port, restTemplate, false);

		ResponseEntity<Void> response = notesApi.deleteVote(noteId.intValue());
		DB_BoardNote dbNote = noteDao.findById(noteId).get();

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(originalVotes - 1, dbNote.getVotes());
	}
	
	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	public void testDeleteVoteConflict() {
		Long noteId = 41L;
		Long originalVotes = 0L;
		NotesApi notesApi = testUtils.createClientApi(NotesApi.class, port, restTemplate, false);

		ResponseEntity<Void> response = notesApi.deleteVote(noteId.intValue());
		DB_BoardNote dbNote = noteDao.findById(noteId).get();

		Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		Assertions.assertEquals(originalVotes, dbNote.getVotes());
	}

	@Test
	public void testDeleteVoteNotFound() {
		NotesApi notesApi = testUtils.createClientApi(NotesApi.class, port, restTemplate, false);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, notesApi.deleteVote(-1).getStatusCode());
	}

	@Test
	@Sql(scripts = { TestUtils.TEST_DATA_PATH })
	public void testPostVoteSuccess() {
		Long noteId = 42L;
		Long originalVotes = 2L;
		NotesApi notesApi = testUtils.createClientApi(NotesApi.class, port, restTemplate, false);

		ResponseEntity<Void> response = notesApi.postVote(noteId.intValue());
		DB_BoardNote dbNote = noteDao.findById(noteId).get();

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(originalVotes + 1, dbNote.getVotes());
	}

	@Test
	public void testPostVoteNotFound() {
		NotesApi notesApi = testUtils.createClientApi(NotesApi.class, port, restTemplate, false);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, notesApi.postVote(-1).getStatusCode());
	}


}
