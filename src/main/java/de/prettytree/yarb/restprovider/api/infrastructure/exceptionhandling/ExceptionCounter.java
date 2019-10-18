package de.prettytree.yarb.restprovider.api.infrastructure.exceptionhandling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class ExceptionCounter {

	private static final int RANDOM_LENGTH = 6;

	private AtomicLong counter;
	private DateTimeFormatter timestampFormatter;

	@PostConstruct
	private void init() {
		// the counter is started at a random long to prohibit identifying the error
		// count of the server runtime
		char[] digits = new char[RANDOM_LENGTH];
		for (int i = 0; i < digits.length; i++) {
			int number = ThreadLocalRandom.current().nextInt(10);
			digits[i] = Character.forDigit(number, 10);
		}
		long counterStart = Long.parseLong(new String(digits));
		counter = new AtomicLong(counterStart);

		timestampFormatter = DateTimeFormatter.ofPattern("YYYYMMddHHmmssSSSS");
	}

	public String getNextExceptionId() {
		String timeStamp = timestampFormatter.format(LocalDateTime.now());
		return timeStamp + "_" + counter.getAndIncrement();
	}
}
