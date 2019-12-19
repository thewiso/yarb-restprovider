package de.prettytree.yarb.restprovider.api.notes;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import de.prettytree.yarb.restprovider.api.NotesApi;
import de.prettytree.yarb.restprovider.api.model.CreateBoardNote;
import de.prettytree.yarb.restprovider.api.model.CreatedResponse;
import de.prettytree.yarb.restprovider.api.model.UpdateBoardNote;
import de.prettytree.yarb.restprovider.db.dao.BoardColumnDao;
import de.prettytree.yarb.restprovider.db.dao.BoardNoteDao;
import de.prettytree.yarb.restprovider.db.model.DB_BoardColumn;
import de.prettytree.yarb.restprovider.db.model.DB_BoardNote;
import de.prettytree.yarb.restprovider.mapping.BoardNoteMapper;

@RestController
public class NotesApiImpl implements NotesApi {

	private BoardColumnDao boardColumnDao;
	private BoardNoteDao boardNoteDao;

	@Autowired
	public NotesApiImpl(BoardColumnDao boardColumnDao, BoardNoteDao boardNoteDao) {
		this.boardColumnDao = boardColumnDao;
		this.boardNoteDao = boardNoteDao;
	}

	// TODO: test
	@Override
	public ResponseEntity<CreatedResponse> createNote(@Valid CreateBoardNote createBoardNote) {
		Optional<DB_BoardColumn> boardColumn = boardColumnDao.findById(createBoardNote.getBoardColumnId().longValue());
		if (boardColumn.isPresent()) {
			DB_BoardNote boardNote = BoardNoteMapper.map(createBoardNote, boardColumn.get());
			boardNoteDao.save(boardNote);

			CreatedResponse retVal = new CreatedResponse();
			retVal.setId(boardNote.getId().intValue());

			return new ResponseEntity<CreatedResponse>(retVal, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// TODO: test
	// TODO: make history?
	@Override
	public ResponseEntity<Void> deleteNote(Integer noteId) {
		Optional<DB_BoardNote> boardNote = boardNoteDao.findById(noteId.longValue());
		if (boardNote.isPresent()) {
			boardNoteDao.delete(boardNote.get());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// TODO: test
	@Override
	public ResponseEntity<Void> updateNote(Integer noteId, @Valid UpdateBoardNote updateBoardNote) {
		Optional<DB_BoardNote> boardNote = boardNoteDao.findById(noteId.longValue());
		if (boardNote.isPresent()) {

			boardNote.get().setContent(updateBoardNote.getContent());
			boardNoteDao.save(boardNote.get());

			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// TODO: test
	@Override
	public ResponseEntity<Void> deleteVote(Integer noteId) {
		Optional<DB_BoardNote> boardNote = boardNoteDao.findById(noteId.longValue());
		if (boardNote.isPresent()) {
			long votes = boardNote.get().getVotes();

			if (votes > 0) {
				boardNote.get().setVotes(--votes);
				boardNoteDao.save(boardNote.get());

				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// TODO: test
	@Override
	public ResponseEntity<Void> postVote(Integer noteId) {
		Optional<DB_BoardNote> boardNote = boardNoteDao.findById(noteId.longValue());
		if (boardNote.isPresent()) {
			long votes = boardNote.get().getVotes();

			boardNote.get().setVotes(++votes);
			boardNoteDao.save(boardNote.get());

			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
