package de.prettytree.yarb.restprovider.api;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.prettytree.yarb.restprovider.api.model.LoginData;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.test.TestUtils;

@RunWith(Arquillian.class)
public class JWTAuthentificationTest {  
	
	@Deployment
	public static WebArchive createDeployment() {
		File[] files = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeAndTestDependencies().resolve()
				.withTransitivity().asFile();

		return ShrinkWrap.create(WebArchive.class, JWTAuthentificationTest.class.getSimpleName() + ".war")
				.addPackages(true, "de.prettytree.yarb.restprovider")
				.addAsResource("persistence.xml", "META-INF/persistence.xml")
				.addAsResource("yarb-jwt.keystore")
	            .addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"))
	            .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-web.xml"))
				.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml")).addAsLibraries(files);
	}
	
	@ArquillianResource
    private URL contextPath;

	@Test
	public void testGetUsersSuccess() {
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(contextPath + "yarb");

        UsersApi usersApi = target.proxy(UsersApi.class);
        
		UserCredentials credentials = new UserCredentials();
		credentials.setUsername(TestUtils.getRandomStringAlphabetic10().toLowerCase());
		credentials.setPassword(TestUtils.getRandomString20());
		usersApi.createUser(credentials);

		AuthApi authApi = target.proxy(AuthApi.class);
		LoginData loginData = authApi.login(credentials);
		
		UsersApi authorisedUsersApi = target.register(new ClientRequestFilter() {
			
			@Override
			public void filter(ClientRequestContext requestContext) throws IOException {
				requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer: " + loginData.getToken());
			}
		}).proxy(UsersApi.class);
		
		
		authorisedUsersApi.getUser(loginData.getUser().getId());
	}

	@Test(expected = NotAuthorizedException.class)
	public void testGetUsersError() {
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(contextPath + "yarb");

        UsersApi usersApi = target.proxy(UsersApi.class);
        
        usersApi.getUser(42);
	}
	

}
