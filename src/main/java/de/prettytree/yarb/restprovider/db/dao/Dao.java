package de.prettytree.yarb.restprovider.db.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

@ApplicationScoped
@Transactional
public abstract class Dao<DbModelClass> {
	// TODO: MULTITHREADING-FÄHIG?!
	@PersistenceContext
	protected EntityManager em;
	
    @Resource
    private UserTransaction userTransaction;

	protected CriteriaBuilder criteriaBuilder;
	
	private Class<DbModelClass> modelClass;

	public Dao(Class<DbModelClass> modelClass) {
		this.modelClass = modelClass;
	}
	
	@PostConstruct
	public void init() {
		criteriaBuilder = em.getCriteriaBuilder();
	}

	//TODO: TEST
	public Optional<DbModelClass> get(long id){
		DbModelClass user = em.find(modelClass, id);
		return Optional.ofNullable(user);
	}

	//TODO: TEST
	public List<DbModelClass> getAll(){
		CriteriaQuery<DbModelClass> criteriaQuery = criteriaBuilder.createQuery(modelClass);
		criteriaQuery.select(criteriaQuery.from(modelClass));
		List<DbModelClass> retVal = em.createQuery(criteriaQuery).getResultList();
		return retVal;
	}

	//TODO: TEST
	public void save(DbModelClass dbModelClass) {
		em.persist(dbModelClass);
	}

//	public abstract void update(DbModelClass dbModelClass, String[] params);

	public void delete(DbModelClass dbModelClass) {
		em.remove(dbModelClass);
	}

	//TODO: TEST
	protected Optional<DbModelClass> getSingleResultOptional(TypedQuery<DbModelClass> query) {
		List<DbModelClass> results = query.getResultList();
		Optional<DbModelClass> retVal;
		if (results.isEmpty()) {
			retVal = Optional.empty();
		} else if (results.size() == 1) {
			retVal = Optional.of(results.get(0));
		} else {
			throw new NonUniqueResultException();
		}
		return retVal;
	}
}
