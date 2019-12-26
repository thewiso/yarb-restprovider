package de.prettytree.yarb.restprovider.test;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class RestProxyInvocationHandler implements InvocationHandler {

	private final String hostUrl;
	private final TestRestTemplate restTemplate;
	private final HttpHeaders headers;

	public RestProxyInvocationHandler(String hostUrl, TestRestTemplate restTemplate) {
		this(hostUrl, restTemplate, null);
	}

	public RestProxyInvocationHandler(String hostUrl, TestRestTemplate restTemplate, HttpHeaders headers) {
		this.hostUrl = hostUrl;
		this.restTemplate = restTemplate;
		this.headers = headers;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		if (requestMapping != null) {
			String invocationUrl = hostUrl + requestMapping.value()[0];
			Object requestObject = null;
			Map<String, String> urlVariables = new HashMap<>();
			
			Parameter[] parameters = method.getParameters();
			//collect the parameters with REST annotations
			for (int i = 0; i < parameters.length; i++) {
				Parameter parameter = parameters[i];
				
				RequestParam requestParameter = parameter.getAnnotation(RequestParam.class);
				if (requestParameter != null) {
					urlVariables.put(requestParameter.value(), args[i].toString());
				}
				
				PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
				if(pathVariable != null) {
					urlVariables.put(pathVariable.value(), args[i].toString());
				}

				RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
				if (requestBody != null) {
					requestObject = args[i];
				}
			}

			ResolvableType methodReturnType = ResolvableType.forMethodReturnType(method).getGeneric(0);
			Class<?> responseType = methodReturnType.resolve();
			HttpEntity<?> requestEntity = new HttpEntity(requestObject, headers);

			if (List.class.isAssignableFrom(responseType)) {
				Class<?> collectionClass = methodReturnType.resolveGeneric(0);
				responseType = Array.newInstance(collectionClass, 0).getClass();
			}

			return restTemplate.exchange(invocationUrl, map(requestMapping.method()[0]), requestEntity, responseType,
					urlVariables);
		}
		throw new UnsupportedOperationException("Called method does not provide a valid REST interface");
	}

	private static HttpMethod map(RequestMethod method) {
		return HttpMethod.valueOf(method.name());
	}

}