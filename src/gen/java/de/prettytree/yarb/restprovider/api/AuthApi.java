package de.prettytree.yarb.restprovider.api;

import de.prettytree.yarb.restprovider.api.model.InternalErrorMessage;
import de.prettytree.yarb.restprovider.api.model.LoginData;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/auth")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2019-10-20T12:28:50.366+02:00[Europe/Berlin]")
public interface AuthApi {

    @POST
    @Path("/login")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    LoginData login(@Valid UserCredentials userCredentials);
}
