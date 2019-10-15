package de.prettytree.yarb.restprovider;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/yarb")
@DeclareRoles({ YARBRestProvider.USER_GROUP })
public class YARBRestProvider extends Application {
	public static final String USER_GROUP = "user";

	
}
//https://openapi-generator.tech/docs/generators/jaxrs-spec
//openapi-generator generate -o javagen -i openapi.yaml -g jaxrs-spec --additional-properties=modelPackage=de.prettytree.yarb.restprovider.user.model,apiPackage=de.prettytree.yarb.restprovider.user.api,dateLibrary=java8,useSwaggerAnnotations=false,interfaceOnly=true
//openapi-generator generate -o javagen -i openapi.yaml -g jaxrs-spec --additional-properties=modelPackage=de.prettytree.yarb.restprovider.authentication.model,apiPackage=de.prettytree.yarb.restprovider.authentication.api,dateLibrary=java8,useSwaggerAnnotations=false,interfaceOnly=true
//openapi-generator generate -o javagen -i openapi.yaml -g jaxrs-spec --additional-properties=modelPackage=de.prettytree.yarb.restprovider.board.model,apiPackage=de.prettytree.yarb.restprovider.board.api,dateLibrary=java8,useSwaggerAnnotations=false,interfaceOnly=true