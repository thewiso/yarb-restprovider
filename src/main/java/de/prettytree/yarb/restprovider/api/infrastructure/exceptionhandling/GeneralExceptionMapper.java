package de.prettytree.yarb.restprovider.api.infrastructure.exceptionhandling;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prettytree.yarb.restprovider.api.model.InternalErrorMessage;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Throwable> {
	
	private static final Logger LOG = LoggerFactory.getLogger(GeneralExceptionMapper.class);
	
	@Inject
	ExceptionCounter exceptionCounter;
	
	
	@Override
	public Response toResponse(Throwable throwable) {
		String exceptionId = exceptionCounter.getNextExceptionId();
		LOG.error(exceptionId + " Error while dealing with request", throwable);
		
		InternalErrorMessage message = new InternalErrorMessage();
		message.setExceptionId(exceptionId);
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).build();
	}

}
