package de.prettytree.yarb.restprovider.api.board;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import de.prettytree.yarb.restprovider.api.BoardsApi;
import de.prettytree.yarb.restprovider.api.model.Board;
import de.prettytree.yarb.restprovider.api.model.CreateBoard;
import de.prettytree.yarb.restprovider.api.model.CreatedResponse;
import de.prettytree.yarb.restprovider.db.dao.BoardDao;
import de.prettytree.yarb.restprovider.db.dao.UserDao;
import de.prettytree.yarb.restprovider.db.model.DB_Board;
import de.prettytree.yarb.restprovider.db.model.DB_User;
import de.prettytree.yarb.restprovider.mapping.BoardMapper;

@RestController
public class BoardsApiImpl implements BoardsApi {

	private BoardDao boardDao;
	private UserDao userDao;

	@Autowired
	public BoardsApiImpl(BoardDao boardDao, UserDao userDao) {
		this.boardDao = boardDao;
		this.userDao = userDao;
	}

	// TODO: test for boardColumns
	@Override
	public ResponseEntity<List<Board>> getBoardsByOwner(Integer userId) {
		if (SecurityContextHolder.getContext().getAuthentication().getName().equals(userId.toString())) {
			List<Board> retVal = boardDao.findByOwnerId(userId.longValue())
					.stream()
					.map(dbBoard -> BoardMapper.map(dbBoard))
					.collect(Collectors.toList());
			return new ResponseEntity<List<Board>>(retVal, HttpStatus.OK);
		}
		return new ResponseEntity<List<Board>>(HttpStatus.FORBIDDEN);
	}

	// TODO: test
	@Override
	public ResponseEntity<CreatedResponse> createBoard(@Valid CreateBoard createBoard) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<DB_User> user = userDao.findById(Long.valueOf(userId));
		
		DB_Board newBoard = BoardMapper.map(createBoard, user.get());
		boardDao.save(newBoard);
		
		CreatedResponse responseEntity = new CreatedResponse();
		responseEntity.setId(newBoard.getId().intValue());
		return new ResponseEntity<CreatedResponse>(responseEntity, HttpStatus.CREATED); 
	}

	//TODO: test
	@Override
	public ResponseEntity<Board> getBoard(Integer boardId) {
		Optional<DB_Board> board = boardDao.findById(boardId.longValue());
		if(board.isPresent()) {
			Board retVal = BoardMapper.map(board.get());
			return new ResponseEntity<Board>(retVal, HttpStatus.OK);
		}else {
			return new ResponseEntity<Board>(HttpStatus.NOT_FOUND);
		}
	}
}
