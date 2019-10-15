package de.prettytree.yarb.restprovider.api.infrastructure.exceptionhandling;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@ApplicationException
public class HttpResponseException extends WebApplicationException {

	private static final long serialVersionUID = 7415242022544412892L;

	public HttpResponseException() {
		super();
	}

	public HttpResponseException(Response response) {
		super(response);
	}

	public HttpResponseException(Status status) {
		super(status);
	}

	public HttpResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpResponseException(Throwable cause) {
		super(cause);
	}

	
}
