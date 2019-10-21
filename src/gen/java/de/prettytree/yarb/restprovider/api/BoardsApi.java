package de.prettytree.yarb.restprovider.api;

import de.prettytree.yarb.restprovider.api.model.Board;
import de.prettytree.yarb.restprovider.api.model.InternalErrorMessage;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/boards")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2019-10-20T12:28:50.366+02:00[Europe/Berlin]")
public interface BoardsApi {

    @GET
    @Produces({ "application/json" })
    List<Board> getBoards(@QueryParam("userId")    Integer userId);
}
