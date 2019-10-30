package de.prettytree.yarb.restprovider.api.infrastructure.exceptionhandling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.prettytree.yarb.restprovider.api.model.InternalErrorMessage;

@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GeneralExceptionHandler.class);
	private ExceptionCounter exceptionCounter;

	@Autowired
	public GeneralExceptionHandler(ExceptionCounter exceptionCounter) {
		this.exceptionCounter = exceptionCounter;
	}

	@ExceptionHandler(value = { Throwable.class })
	protected ResponseEntity<InternalErrorMessage> handleException(Throwable throwable, WebRequest request) {
		String exceptionId = exceptionCounter.getNextExceptionId();
		LOG.error(exceptionId + " Error while dealing with request", throwable);

		InternalErrorMessage message = new InternalErrorMessage();
		message.setExceptionId(exceptionId);

		return new ResponseEntity<InternalErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
