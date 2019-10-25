package de.prettytree.yarb.restprovider.api.authentication;

import java.io.File;
import java.net.URL;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.prettytree.yarb.restprovider.api.AuthApi;
import de.prettytree.yarb.restprovider.api.model.User;
import de.prettytree.yarb.restprovider.api.model.UserCredentials;
import de.prettytree.yarb.restprovider.test.TestUtils;

@RunWith(Arquillian.class)
public class AuthApiImplTest {

	@Inject
	private AuthApi authApi;

	@ArquillianResource
	private URL contextPath;

	@Deployment
	public static WebArchive createDeployment() {
		File[] files = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeAndTestDependencies().resolve()
				.withTransitivity().asFile();

		return ShrinkWrap.create(WebArchive.class, AuthApiImplTest.class.getSimpleName() + ".war")
				.addPackages(true, "de.prettytree.yarb.restprovider").addAsResource("yarb-jwt.keystore")
				.addAsResource("persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml")).addAsLibraries(files);
	}

	@Before
	public void init() {
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(contextPath + "yarb");

		authApi = target.proxy(AuthApi.class);
	}

	@Test
	public void testLoginWithoutUser() {
		UserCredentials userCredentials = new UserCredentials();
		userCredentials.setPassword(TestUtils.getRandomString20());
		userCredentials.setUsername(TestUtils.getRandomStringAlphabetic10().toLowerCase());
		WebApplicationException exception = TestUtils.assertThrowsException(() -> {
			authApi.login(userCredentials);
		}, NotAuthorizedException.class);
	}

	@UsingDataSet("datasets/users_mrfoo.xml")
	@Test
	public void testLoginWithUser() throws Throwable {
		UserCredentials userCredentials = TestUtils.getMrFooCredentials();
		User user = authApi.login(userCredentials).getUser();

		Assert.assertEquals(1, user.getId().longValue());
		Assert.assertEquals("mrfoo", user.getUsername());
	}
}
