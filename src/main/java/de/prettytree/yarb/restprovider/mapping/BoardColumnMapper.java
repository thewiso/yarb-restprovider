package de.prettytree.yarb.restprovider.mapping;

import java.util.List;
import java.util.stream.Collectors;

import de.prettytree.yarb.restprovider.api.model.BoardColumn;
import de.prettytree.yarb.restprovider.api.model.BoardNote;
import de.prettytree.yarb.restprovider.db.model.DB_Board;
import de.prettytree.yarb.restprovider.db.model.DB_BoardColumn;

public class BoardColumnMapper {

	//TODO: test
	public static DB_BoardColumn map(String name, DB_Board board) {
		DB_BoardColumn retVal = new DB_BoardColumn();
		
		retVal.setName(name);
		retVal.setBoard(board);
		
		return retVal;
	}
	
	//TODO: test
	public static BoardColumn map(DB_BoardColumn column) {
		BoardColumn retVal = new BoardColumn();
		
		retVal.setId(column.getId().intValue());
		retVal.setName(column.getName());
		
		List<BoardNote> notes = column.getBoardNotes().stream().map(note -> BoardNoteMapper.map(note)).collect(Collectors.toList());
		retVal.setNotes(notes);
		
		return retVal;
	}
}
