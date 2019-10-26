package de.prettytree.yarb.restprovider.db.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.prettytree.yarb.restprovider.db.model.DB_Board;

public class BoardDao extends Dao<DB_Board> {

	public BoardDao() {
		super(DB_Board.class);
	}
	
	public List<DB_Board> findByUserId(long userId){
		CriteriaQuery<DB_Board> criteriaQuery = criteriaBuilder.createQuery(DB_Board.class);
		Root<DB_Board> boardRoot = criteriaQuery.from(DB_Board.class);
		criteriaQuery.select(boardRoot).where(criteriaBuilder.equal(boardRoot.get("owner"), userId));
		TypedQuery<DB_Board> query = em.createQuery(criteriaQuery);
		return query.getResultList();
	}

}
