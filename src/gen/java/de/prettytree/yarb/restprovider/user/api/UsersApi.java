package de.prettytree.yarb.restprovider.user.api;

import de.prettytree.yarb.restprovider.user.model.PasswordWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/users")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2019-10-05T21:49:38.310+02:00[Europe/Berlin]")
public interface UsersApi {

    @PUT
    @Path("/{username}")
    @Consumes({ "application/json" })
    Response usersUsernamePut(@PathParam("username") @Pattern(regexp="^[\\S]+$") @Size(min=4,max=20) String username,@Valid PasswordWrapper passwordWrapper);
}
