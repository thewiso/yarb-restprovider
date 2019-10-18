package de.prettytree.yarb.restprovider.test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;

import org.apache.commons.lang3.RandomStringUtils;

public class TestUtils {

	public static LocalDateTime getRandomLocalDateTime() {
		long timeStamp = ThreadLocalRandom.current().nextLong(-System.currentTimeMillis(), System.currentTimeMillis());
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneId.systemDefault());
	}

	public static double getRandomDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}

	public static boolean getRandomBool() {
		return ThreadLocalRandom.current().nextBoolean();
	}

	public static String getRandomString10() {
		return RandomStringUtils.random(10);
	}

	public static String getRandomString20() {
		return RandomStringUtils.random(20);
	}
	
	public static String getRandomStringAlphabetic10() {
		return RandomStringUtils.randomAlphabetic(10);
	}

	public static byte[] getRandomByteArray() {
		return RandomStringUtils.random(20).getBytes(StandardCharsets.UTF_8);
	}
	
	public static long getRandomLong() {
		return ThreadLocalRandom.current().nextLong();
	}
	
	public static int getRandomInt() {
		return ThreadLocalRandom.current().nextInt();
	}
	
	public static <EntityClass> void truncateTable(EntityManager em, Class<EntityClass> entityClass) {
		CriteriaBuilder criterialBuilder = em.getCriteriaBuilder();
		CriteriaDelete<EntityClass> query = criterialBuilder.createCriteriaDelete(entityClass);
		query.from(entityClass);
		em.createQuery(query).executeUpdate();
	}

}
