/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (4.1.3).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package de.prettytree.yarb.restprovider.api;

import de.prettytree.yarb.restprovider.api.model.Board;
import de.prettytree.yarb.restprovider.api.model.CreateBoard;
import de.prettytree.yarb.restprovider.api.model.CreatedResponse;
import de.prettytree.yarb.restprovider.api.model.InternalErrorMessage;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-10-29T07:51:26.217643+01:00[Europe/Berlin]")

@Validated
@Api(value = "boards", description = "the boards API")
public interface BoardsApi {

    @ApiOperation(value = "createBoard", nickname = "createBoard", notes = "Create new board", response = CreatedResponse.class, authorizations = {
        @Authorization(value = "bearerAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = CreatedResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized") })
    @RequestMapping(value = "/boards",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<CreatedResponse> createBoard(@ApiParam(value = ""  )  @Valid @RequestBody CreateBoard createBoard);


    @ApiOperation(value = "getBoards", nickname = "getBoards", notes = "Get boards by owner", response = Board.class, responseContainer = "List", authorizations = {
        @Authorization(value = "bearerAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Board.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 500, message = "Internal Server Error", response = InternalErrorMessage.class) })
    @RequestMapping(value = "/boards",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Board>> getBoards(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "userId", required = true) Integer userId);

}
