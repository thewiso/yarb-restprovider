package de.prettytree.yarb.restprovider.api.board;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.ForbiddenException;

import de.prettytree.yarb.restprovider.api.BoardsApi;
import de.prettytree.yarb.restprovider.api.model.Board;
import de.prettytree.yarb.restprovider.db.dao.BoardDao;
import de.prettytree.yarb.restprovider.mapping.BoardMapper;

public class BoardsApiImpl implements BoardsApi {

	@Inject
	private SecurityContext securityContext;

	@Inject
	private BoardDao boardDao;

	@Inject
	private BoardMapper boardMapper;

	// TODO: test
	@Override
	public List<Board> getBoards(Integer userId) {
		if (securityContext.getCallerPrincipal().getName().equals(userId.toString())) {
			return boardDao.findByUserId(userId.longValue())
					.stream()
					.map(dbUser -> boardMapper.map(dbUser))
					.collect(Collectors.toList());
		}
		throw new ForbiddenException();
	}

}
