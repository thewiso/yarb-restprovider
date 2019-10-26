package de.prettytree.yarb.restprovider.db.dao;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.prettytree.yarb.restprovider.db.model.TestEntity;
import de.prettytree.yarb.restprovider.test.TestUtils;

@RunWith(Arquillian.class)
@Transactional
@CleanupUsingScript(TestUtils.CLEANUP_DB_SCRIPT_PATH)
public class DaoTest {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private TestEntityDao dao;

	@Deployment
	public static WebArchive createDeployment() {
		return TestUtils.createDefaultDeployment();
	}

	@Test
	public void testSave() throws Throwable {
		TestEntity testEntity = getRandomEntity();
		dao.save(testEntity);

		Long id = testEntity.getId();
		Assert.assertNotNull(id);

		TestEntity savedEntity = em.find(TestEntity.class, id);
		Assert.assertNotNull(savedEntity);

		assertEqualsTestEntities(testEntity, savedEntity);
	}

	@Test
	public void testFind() throws Throwable {
		TestEntity testEntity = getRandomEntity();

		em.persist(testEntity);
		Long id = testEntity.getId();

		Optional<TestEntity> savedEntityOpt = dao.find(id);
		Assert.assertTrue(savedEntityOpt.isPresent());
		TestEntity savedEntity = savedEntityOpt.get();

		assertEqualsTestEntities(testEntity, savedEntity);
	}
	
	@Test
	public void testFindAll() throws Throwable {
		int count = ThreadLocalRandom.current().nextInt(50) + 10;
		TestEntity[] sourceArray = new TestEntity[count];

		for (int i = 0; i < count; i++) {
			TestEntity entity = getRandomEntity();
			sourceArray[i] = entity;
			em.persist(entity);
		}

		List<TestEntity> resultList = dao.findAll();
		Assert.assertEquals(count, resultList.size());
		resultList.sort(Comparator.comparingLong(TestEntity::getId));

		for (int i = 0; i < count; i++) {
			assertEqualsTestEntities(sourceArray[i], resultList.get(i));
		}
	}

	@Test
	public void testDelete() throws Throwable{
		int count = ThreadLocalRandom.current().nextInt(10, 50);
		int randomIndex = ThreadLocalRandom.current().nextInt(0, count);
		TestEntity[] sourceArray = new TestEntity[count];

		for (int i = 0; i < count; i++) {
			TestEntity entity = getRandomEntity();
			sourceArray[i] = entity;
			dao.save(entity);
		}
		
		TestEntity testEntity = sourceArray[randomIndex];
		dao.delete(testEntity);
		
		Optional<TestEntity> testEntityOpt = dao.find(testEntity.getId());
		Assert.assertFalse(testEntityOpt.isPresent());
		
		Assert.assertEquals(count - 1, dao.findAll().size());
	}

	@Test
	public void testGetSingleResultOptionalNoResult() {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<TestEntity> criteriaQuery = criteriaBuilder.createQuery(TestEntity.class);
		Root<TestEntity> testEntityRoot = criteriaQuery.from(TestEntity.class);
		criteriaQuery.select(testEntityRoot).where(criteriaBuilder.equal(testEntityRoot.get("string"), TestUtils.getRandomString20()));
		TypedQuery<TestEntity> query = em.createQuery(criteriaQuery);
		
		Optional<TestEntity> testEntityOpt = dao.getSingleResultOptional(query);
		Assert.assertFalse(testEntityOpt.isPresent());
	}
	
	@Test
	public void testGetSingleResultOptionalOneResult() {
		TestEntity testEntity = getRandomEntity();
		dao.save(testEntity);
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<TestEntity> criteriaQuery = criteriaBuilder.createQuery(TestEntity.class);
		Root<TestEntity> testEntityRoot = criteriaQuery.from(TestEntity.class);
		criteriaQuery.select(testEntityRoot).where(criteriaBuilder.equal(testEntityRoot.get("string"), testEntity.getString()));
		TypedQuery<TestEntity> query = em.createQuery(criteriaQuery);
		
		Optional<TestEntity> testEntityOpt = dao.getSingleResultOptional(query);
		Assert.assertTrue(testEntityOpt.isPresent());
		assertEqualsTestEntities(testEntity, testEntityOpt.get());
	}
	
	@Test(expected = NonUniqueResultException.class)
	public void testGetSingleResultOptionalMultipleResult() {
		String randomString = TestUtils.getRandomString10();
		TestEntity testEntity = getRandomEntity();
		testEntity.setString(randomString);
		dao.save(testEntity);
		
		testEntity = getRandomEntity();
		testEntity.setString(randomString);
		dao.save(testEntity);
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<TestEntity> criteriaQuery = criteriaBuilder.createQuery(TestEntity.class);
		Root<TestEntity> testEntityRoot = criteriaQuery.from(TestEntity.class);
		criteriaQuery.select(testEntityRoot).where(criteriaBuilder.equal(testEntityRoot.get("string"), randomString));
		TypedQuery<TestEntity> query = em.createQuery(criteriaQuery);
		
		dao.getSingleResultOptional(query);
	}	
	
	private static TestEntity getRandomEntity() {
		TestEntity retVal = new TestEntity();
		
		retVal.setString(TestUtils.getRandomString20());
		retVal.setBool(TestUtils.getRandomBool());
		retVal.setDouble(TestUtils.getRandomDouble());
		retVal.setDate(TestUtils.getRandomLocalDateTime());
		retVal.setByteArray(TestUtils.getRandomByteArray());
		
		return retVal;
	}
	
	private static void assertEqualsTestEntities(TestEntity a, TestEntity b) {
		Assert.assertEquals(a.getId(), b.getId());
		Assert.assertEquals(a.isBool(), b.isBool());
		Assert.assertEquals(a.getDouble(), b.getDouble(), 0);
		Assert.assertEquals(a.getString(), b.getString());
		Assert.assertArrayEquals(a.getByteArray(), b.getByteArray());
		Assert.assertEquals(a.getDate(), b.getDate());
	}

}
