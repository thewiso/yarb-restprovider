/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (4.1.3).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package de.prettytree.yarb.restprovider.api;

import de.prettytree.yarb.restprovider.api.model.InternalErrorMessage;
import de.prettytree.yarb.restprovider.api.model.User;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-22T15:48:57.260+01:00[Europe/Berlin]")

@Validated
@Api(value = "users", description = "the users API")
public interface UsersApi {

    @ApiOperation(value = "createUser", nickname = "createUser", notes = "Create new user", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No Content"),
        @ApiResponse(code = 409, message = "Conflict: username already existing"),
        @ApiResponse(code = 500, message = "Internal Server Error", response = InternalErrorMessage.class) })
    @RequestMapping(value = "/users",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Void> createUser(@ApiParam(value = ""  )  @Valid @RequestBody UserCredentials userCredentials);


    @ApiOperation(value = "getUser", nickname = "getUser", notes = "Get user", response = User.class, authorizations = {
        @Authorization(value = "bearerAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = User.class),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 500, message = "Internal Server Error", response = InternalErrorMessage.class) })
    @RequestMapping(value = "/users/{userId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<User> getUser(@ApiParam(value = "",required=true) @PathVariable("userId") Integer userId);

}
