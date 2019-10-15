package de.prettytree.yarb.restprovider.api;

import de.prettytree.yarb.restprovider.api.model.InternalErrorMessage;
import de.prettytree.yarb.restprovider.api.model.User;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/users")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2019-10-15T22:47:46.308+02:00[Europe/Berlin]")
public interface UsersApi {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    void createUser(@Valid UserCredentials userCredentials);

    @GET
    @Path("/{userId}")
    @Produces({ "application/json" })
    User getUser(@PathParam("userId") Integer userId);
}
