package de.prettytree.yarb.restprovider.infrastructure.exceptionhandling;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

//https://stackoverflow.com/questions/48297120/annotation-for-mandatory-attribute-in-json-validation-in-spring-rest-controller
public class GeneralExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {
		//TODO
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

}
