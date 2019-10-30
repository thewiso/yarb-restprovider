package de.prettytree.yarb.restprovider.api.board;

import java.util.List;
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
import de.prettytree.yarb.restprovider.mapping.BoardMapper;

@RestController
public class BoardsApiImpl implements BoardsApi {
//TODO: only autowired constructors!	
	private BoardDao boardDao;

	@Autowired
	public BoardsApiImpl(BoardDao boardDao) {
		this.boardDao = boardDao;
	}

	// TODO: test for boardColumns
	@Override
	public ResponseEntity<List<Board>> getBoards(Integer userId) {
		if (SecurityContextHolder.getContext().getAuthentication().getName().equals(userId.toString())) {
			List<Board> retVal = boardDao.findByOwner(userId.longValue())
					.stream()
					.map(dbUser -> BoardMapper.map(dbUser))
					.collect(Collectors.toList());
			return new ResponseEntity<List<Board>>(retVal, HttpStatus.OK);
		}
		return new ResponseEntity<List<Board>>(HttpStatus.FORBIDDEN);
	}

	// TODO: test
	@Override
	public ResponseEntity<CreatedResponse> createBoard(@Valid CreateBoard createBoard) {
		return null;
	}
}
