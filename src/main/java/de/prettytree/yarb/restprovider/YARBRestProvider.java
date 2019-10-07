package de.prettytree.yarb.restprovider;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

//TODO: sinnvolle rückmeldung bei fehlerhaftem input!
@ApplicationPath("/yarb")
@DeclareRoles({"protected"})
public class YARBRestProvider extends Application {
}
//https://openapi-generator.tech/docs/generators/jaxrs-spec
//openapi-generator generate -o javagen -i openapi.yaml -g jaxrs-spec --additional-properties=modelPackage=de.prettytree.yarb.restprovider.user.model,apiPackage=de.prettytree.yarb.restprovider.user.api,dateLibrary=java8,useSwaggerAnnotations=false,interfaceOnly=true,returnResponse=true
//openapi-generator generate -o javagen -i openapi.yaml -g jaxrs-spec --additional-properties=modelPackage=de.prettytree.yarb.restprovider.authentication.model,apiPackage=de.prettytree.yarb.restprovider.authentication.api,dateLibrary=java8,useSwaggerAnnotations=false,interfaceOnly=true,returnResponse=true
