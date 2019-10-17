package de.prettytree.yarb.restprovider.db.dao;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.prettytree.yarb.restprovider.db.model.TestEntity;
import de.prettytree.yarb.restprovider.test.TestUtils;

@RunWith(Arquillian.class)
public class DaoTest {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private TestEntityDao dao;

	@Deployment
	public static WebArchive createDeployment() {
		File[] files = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeDependencies().resolve()
				.withTransitivity().asFile();

		return ShrinkWrap.create(WebArchive.class, "test2.war")
				.addPackages(true, "de.prettytree.yarb.restprovider.db")
				.addPackages(true, "de.prettytree.yarb.restprovider.test")
				.addAsResource("persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
				.addAsLibraries(files);
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

	@Transactional
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

	@Transactional
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

	private static TestEntity getRandomEntity() {
		TestEntity retVal = new TestEntity();
		
		retVal.setString("foo");
		retVal.setBool(TestUtils.getRandomBool());
		retVal.setDouble(TestUtils.getRandomDouble());
		retVal.setDate(TestUtils.getRandomLocalDateTime());
		retVal.setByteArray("bar".getBytes(StandardCharsets.UTF_8));
		
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
