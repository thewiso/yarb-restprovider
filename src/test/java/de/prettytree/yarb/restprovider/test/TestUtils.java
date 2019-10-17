package de.prettytree.yarb.restprovider.test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

public class TestUtils {
	
	public static LocalDateTime getRandomLocalDateTime() {
		long timeStamp = ThreadLocalRandom.current().nextLong(LocalDateTime.MIN.toEpochSecond(ZoneOffset.UTC), LocalDateTime.MAX.toEpochSecond(ZoneOffset.UTC));
		return LocalDateTime.ofEpochSecond(timeStamp, 0, ZoneOffset.UTC);
	}
	
	public static double getRandomDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}
	
	public static boolean getRandomBool() {
		return ThreadLocalRandom.current().nextBoolean();
	}
	
	//BYTE[]
	//STRING

	//TODO: alle tests umstellen
	//org.apache.commons.lang3.RandomStringUtils
}
