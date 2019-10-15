package de.prettytree.yarb.restprovider.api.infrastructure.exceptionhandling;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prettytree.yarb.restprovider.api.model.InternalErrorMessage;

@Provider
public class HttpResponseExceptionMapper implements ExceptionMapper<HttpResponseException> {

	private static final Logger LOG = LoggerFactory.getLogger(HttpResponseExceptionMapper.class);

	@Inject
	ExceptionCounter exceptionCounter;
	
	@Override
	public Response toResponse(HttpResponseException exception) {
		if (exception.getResponse().getStatusInfo().getFamily() == Response.Status.Family.SERVER_ERROR) {
			Throwable throwable = exception;
			if(throwable.getCause() != null) {
				throwable = throwable.getCause();
			}
			
			String exceptionId = exceptionCounter.getNextExceptionId();
			LOG.error(exceptionId + " Error while dealing with request. HTTP-Status: " + exception.getResponse().getStatus(),
					throwable);
			
			InternalErrorMessage message = new InternalErrorMessage();
			message.setExceptionId(exceptionId);
			
			return Response.fromResponse(exception.getResponse()).entity(message).build();
		}

		return exception.getResponse();
	}

}
