package de.prettytree.yarb.restprovider.mapping;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import de.prettytree.yarb.restprovider.api.model.Board;
import de.prettytree.yarb.restprovider.db.model.DB_Board;

public class BoardMapper implements ModelMapper<DB_Board, Board> {

	@Override
	public Board map(DB_Board sourceModel) {
		Board retVal = new Board();
		
		retVal.setUserId(sourceModel.getOwner().getId().intValue());//TODO: change yaml datatype
		retVal.setCreationDate(OffsetDateTime.of(sourceModel.getCreatedAt(), ZoneOffset.UTC));
		retVal.setId(sourceModel.getId().intValue());
		retVal.setName(sourceModel.getName());
		
		return retVal;
	}

}
