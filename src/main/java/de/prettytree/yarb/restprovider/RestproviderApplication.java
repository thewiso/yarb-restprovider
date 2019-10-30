package de.prettytree.yarb.restprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class RestproviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestproviderApplication.class, args);
	}

}
