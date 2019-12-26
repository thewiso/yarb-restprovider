package de.prettytree.yarb.restprovider.mapping;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import de.prettytree.yarb.restprovider.api.model.Board;
import de.prettytree.yarb.restprovider.api.model.BoardColumn;
import de.prettytree.yarb.restprovider.api.model.CreateBoard;
import de.prettytree.yarb.restprovider.db.model.DB_Board;
import de.prettytree.yarb.restprovider.db.model.DB_BoardColumn;
import de.prettytree.yarb.restprovider.db.model.DB_User;

public class BoardMapper {

	public static Board map(DB_Board sourceModel) {
		Board retVal = new Board();

		retVal.setCreationDate(OffsetDateTime.of(sourceModel.getCreatedAt(), ZoneOffset.UTC));
		retVal.setId(sourceModel.getId().intValue());
		retVal.setName(sourceModel.getName());

		List<BoardColumn> columns = sourceModel.getBoardColumns()
				.stream()
				.map(board -> BoardColumnMapper.map(board))
				.collect(Collectors.toList());
		
		retVal.setColumns(columns);

		return retVal;
	}

	public static DB_Board map(CreateBoard createBoard, DB_User owner) {
		DB_Board retVal = new DB_Board();

		retVal.setName(createBoard.getName());
		retVal.setOwner(owner);
		retVal.setCreatedAt(LocalDateTime.now());

		List<DB_BoardColumn> columns = createBoard.getColumnNames()
				.stream()
				.map(column -> BoardColumnMapper.map(column, retVal))
				.collect(Collectors.toList());
		retVal.setBoardColumns(columns);

		return retVal;
	}

}
