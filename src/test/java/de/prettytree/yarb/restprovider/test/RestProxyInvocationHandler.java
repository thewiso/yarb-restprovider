package de.prettytree.yarb.restprovider.test;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

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
			String baseUrl = hostUrl + requestMapping.value()[0];
			Object requestObject = null;
			Map<String, String> urlVariables = new HashMap<>();
			UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl);
			boolean isListResponseType = false;

			// collect the input parameters
			Parameter[] parameters = method.getParameters();
			for (int i = 0; i < parameters.length; i++) {
				Parameter parameter = parameters[i];

				RequestParam requestParameter = parameter.getAnnotation(RequestParam.class);
				if (requestParameter != null) {
					urlBuilder.queryParam(requestParameter.value(), args[i].toString());
				}

				PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
				if (pathVariable != null) {
					urlVariables.put(pathVariable.value(), args[i].toString());
				}

				RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
				if (requestBody != null) {
					requestObject = args[i];
				}
			}

			// find out the response type
			ResolvableType methodReturnType = ResolvableType.forMethodReturnType(method).getGeneric(0);
			Class<?> responseType = methodReturnType.resolve();
			HttpEntity<?> requestEntity = new HttpEntity(requestObject, headers);

			if (List.class.isAssignableFrom(responseType)) {
				Class<?> collectionClass = methodReturnType.resolveGeneric(0);
				responseType = Array.newInstance(collectionClass, 0).getClass();
				isListResponseType = true;
			}

			// make service call
			String invocationUrl = urlBuilder.build(urlVariables).toString();
			ResponseEntity response = restTemplate.exchange(invocationUrl, mapMethod(requestMapping.method()[0]),
					requestEntity, responseType);

			// if necessary, parse response array to list
			if (isListResponseType && response.getBody() != null) {
				List responseList = Arrays.asList((Object[]) response.getBody());
				response = new ResponseEntity(responseList, response.getHeaders(), response.getStatusCode());
			}

			return response;
		}
		throw new UnsupportedOperationException("Called method does not provide a valid REST interface");
	}

	private static HttpMethod mapMethod(RequestMethod method) {
		return HttpMethod.valueOf(method.name());
	}

}