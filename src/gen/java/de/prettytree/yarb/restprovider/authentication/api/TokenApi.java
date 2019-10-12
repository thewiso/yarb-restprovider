package de.prettytree.yarb.restprovider.authentication.api;

import de.prettytree.yarb.restprovider.authentication.model.LoginCredentials;
import de.prettytree.yarb.restprovider.authentication.model.Token;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/token")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2019-10-08T22:07:53.192+02:00[Europe/Berlin]")
public interface TokenApi {

    @POST
    @Path("/create")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    Response tokenCreatePost(@Valid LoginCredentials loginCredentials);
}
