package io.mosip.kernel.idobjectvalidator.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.http.ResponseWrapper;

/**
 * @author Manoj SP
 *
 */
@Configuration
public class TestConfig {

	ObjectMapper mapper = new ObjectMapper();

	@Bean
	public RestTemplate restTemplate()
			throws RestClientException, JsonParseException, JsonMappingException, IOException {
		mapper.registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());
		RestTemplate restTemplate = mock(RestTemplate.class);
		mockLangResponse(restTemplate);
		mockGenderResponse(restTemplate);
		mockLocationResponse(restTemplate);
		mockLocationHierarchyDistrict(restTemplate);
		mockLocationHierarchyPrefectureResponse(restTemplate);
		mockLocationHierarchyRegionResponse(restTemplate);
		mockLocationHierarchySectorResponse(restTemplate);
		mockLocationHierarchySousPrefectureOrCommuneResponse(restTemplate);
		mockDocumentCategoriesResponse(restTemplate);
		mockDocumentTypesResponse(restTemplate);
		mockResidenceStatusResponse(restTemplate);
		return restTemplate;
	}

	private void mockLangResponse(RestTemplate restTemplate)
			throws RestClientException, JsonParseException, JsonMappingException, IOException {
		String response = "{\"id\":null,\"version\":null,\"responsetime\":\"2019-05-21T05:37:06.663Z\",\"metadata\":null,\"response\":{\"languages\":[{\"code\":\"fra\",\"name\":\"Francais\",\"family\":\"Indo-European\",\"nativeName\":\"Francais\",\"isActive\":true}]},\"errors\":null}";
		when(restTemplate.getForObject("https://0.0.0.0/language", ObjectNode.class))
				.thenReturn(mapper.readValue(response.getBytes(), ObjectNode.class));
	}

	private void mockGenderResponse(RestTemplate restTemplate)
			throws RestClientException, JsonParseException, JsonMappingException, IOException {
		String response = "{\"id\":null,\"version\":null,\"responsetime\":\"2019-05-21T05:37:06.813Z\",\"metadata\":null,\"response\":{\"genderType\":[{\"code\":\"MLE\",\"genderName\":\"Masculin\",\"langCode\":\"fra\",\"isActive\":true}]},\"errors\":null}";
		when(restTemplate.getForObject("https://0.0.0.0/gendertypes", ResponseWrapper.class))
				.thenReturn(mapper.readValue(response.getBytes(), ResponseWrapper.class));
	}

	private void mockLocationResponse(RestTemplate restTemplate)
			throws RestClientException, JsonParseException, JsonMappingException, IOException {
		String response = "{\"id\":null,\"version\":null,\"responsetime\":\"2019-05-21T05:37:07.582Z\","
				+ "\"metadata\":null,\"response\":"
				+ "{\"locations\":["
				+ "{\"locationHierarchylevel\":5,\"locationHierarchyName\":\"SECTEUR\",\"isActive\":true},"
				+ "{\"locationHierarchylevel\":1,\"locationHierarchyName\":\"REGION\",\"isActive\":true},"
				+ "{\"locationHierarchylevel\":2,\"locationHierarchyName\":\"PREFECTURE\",\"isActive\":true},"
				+ "{\"locationHierarchylevel\":4,\"locationHierarchyName\":\"DISTRICT\",\"isActive\":true},"
				+ "{\"locationHierarchylevel\":3,\"locationHierarchyName\":\"SOUS_PREFECTURE/COMMUNE\",\"isActive\":true}]},\"errors\":null}";
		ResponseWrapper<ObjectNode> responseWrapper = mapper.readValue(response.getBytes(),
				new TypeReference<ResponseWrapper<ObjectNode>>() {
				});
		when(restTemplate.exchange("https://0.0.0.0/locations/fra", HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseWrapper<ObjectNode>>() {
				})).thenReturn(new ResponseEntity<ResponseWrapper<ObjectNode>>(responseWrapper, HttpStatus.OK));
	}

	private void mockLocationHierarchyDistrict(RestTemplate restTemplate)
			throws RestClientException, JsonParseException, JsonMappingException, IOException {
		String response = "{\"id\":null,\"version\":null,\"responsetime\":\"2019-05-21T05:37:08.486Z\","
				+ "\"metadata\":null,\"response\":{\"locations\":"
				+ "[{\"code\":\"ley sougue\",\"name\":\"Ley Sougue\","
				+ "\"hierarchyLevel\":0,\"hierarchyName\":\"DISTRICT\","
				+ "\"parentLocCode\":\"pilimini\",\"langCode\":"
				+ "\"fra\",\"isActive\":true}]},\"errors\":null}";
		when(restTemplate.getForObject("https://0.0.0.0/locationhierarchy/DISTRICT",
				ResponseWrapper.class)).thenReturn(mapper.readValue(response.getBytes(), ResponseWrapper.class));
	}

	private void mockLocationHierarchyPrefectureResponse(RestTemplate restTemplate)
			throws RestClientException, JsonParseException, JsonMappingException, IOException {
		String response = "{\"id\":null,\"version\":null,\"responsetime\":\"2019-05-21T05:37:09.945Z\",\"metadata\":null,\"response\":{\"locations\":[{\"code\":\"2\",\"name\":\"Koubia\",\"hierarchyLevel\":0,\"hierarchyName\":\"PREFECTURE\",\"parentLocCode\":\"30\",\"langCode\":\"fra\",\"isActive\":true}]},\"errors\":null}";
		when(restTemplate.getForObject("https://0.0.0.0/locationhierarchy/PREFECTURE", ResponseWrapper.class))
				.thenReturn(mapper.readValue(response.getBytes(), ResponseWrapper.class));
	}

	private void mockLocationHierarchyRegionResponse(RestTemplate restTemplate)
			throws RestClientException, JsonParseException, JsonMappingException, IOException {
		String response = "{\"id\":null,\"version\":null,\"responsetime\":\"2019-05-22T06:49:50.975Z\",\"metadata\":null,\"response\":{\"locations\":[{\"code\":\"30\",\"name\":\"Labé\",\"hierarchyLevel\":0,\"hierarchyName\":\"REGION\",\"parentLocCode\":\"GN\",\"langCode\":\"fra\",\"isActive\":true}]},\"errors\":null}";
		when(restTemplate.getForObject("https://0.0.0.0/locationhierarchy/REGION", ResponseWrapper.class))
				.thenReturn(mapper.readValue(response.getBytes(), ResponseWrapper.class));
	}

	private void mockLocationHierarchySectorResponse(RestTemplate restTemplate)
			throws RestClientException, JsonParseException, JsonMappingException, IOException {
		String response = "{\"id\":null,\"version\":null,\"responsetime\":\"2019-05-22T06:49:50.975Z\",\"metadata\":null,\"response\":{\"locations\":[{\"code\":\"missira\",\"name\":\"Missira\",\"hierarchyLevel\":0,\"hierarchyName\":\"SECTEUR\",\"parentLocCode\":\"ley sougue\",\"langCode\":\"fra\",\"isActive\":true}]},\"errors\":null}";
		when(restTemplate.getForObject("https://0.0.0.0/locationhierarchy/SECTEUR", ResponseWrapper.class))
				.thenReturn(mapper.readValue(response.getBytes(), ResponseWrapper.class));
	}

	private void mockLocationHierarchySousPrefectureOrCommuneResponse(RestTemplate restTemplate)
			throws RestClientException, JsonParseException, JsonMappingException, IOException {
		String response = "{\"id\":null,\"version\":null,\"responsetime\":\"2019-05-22T06:49:51.552Z\",\"metadata\":null,\"response\":{\"locations\":[{\"code\":\"pilimini\",\"name\":\"Pilimini\",\"hierarchyLevel\":0,\"hierarchyName\":\"City\",\"parentLocCode\":\"2\",\"langCode\":\"fra\",\"isActive\":true}]},\"errors\":null}";
		when(restTemplate.getForObject("https://0.0.0.0/locationhierarchy/SOUS_PREFECTURE/COMMUNE", ResponseWrapper.class))
				.thenReturn(mapper.readValue(response.getBytes(), ResponseWrapper.class));
	}

	private void mockDocumentCategoriesResponse(RestTemplate restTemplate)
			throws RestClientException, JsonParseException, JsonMappingException, IOException {
		String response = "{\"id\":null,\"version\":null,\"responsetime\":\"2019-05-21T05:37:10.030Z\",\"metadata\":null,\"response\":{\"documentcategories\":[{\"code\":\"POI\",\"name\":\"proofOfIdentity\",\"description\":\"Identity Proof\",\"langCode\":\"fra\",\"isActive\":true}]},\"errors\":null}";
		when(restTemplate.getForObject("https://0.0.0.0/documentcategories", ResponseWrapper.class))
				.thenReturn(mapper.readValue(response.getBytes(), ResponseWrapper.class));
	}

	private void mockDocumentTypesResponse(RestTemplate restTemplate)
			throws RestClientException, JsonParseException, JsonMappingException, IOException {
		String response = "{\"id\":null,\"version\":null,\"responsetime\":\"2019-05-21T05:37:16.097Z\",\"metadata\":null,\"response\":{\"documents\":[{\"code\":\"Carte d’Identité\",\"name\":\"Carte d’Identité\",\"description\":\"Proof of Idendity\",\"langCode\":\"fra\",\"isActive\":true}]},\"errors\":null}";
		when(restTemplate.getForObject("https://0.0.0.0/documenttypes", ResponseWrapper.class))
				.thenReturn(mapper.readValue(response.getBytes(), ResponseWrapper.class));
	}

	private void mockResidenceStatusResponse(RestTemplate restTemplate)
			throws RestClientException, JsonParseException, JsonMappingException, IOException {
		String response = "{\"id\":null,\"version\":null,\"responsetime\":\"2019-12-31T05:36:29.858Z\",\"metadata\":null,\"response\":{\"individualTypes\":[{\"code\":\"GN\",\"langCode\":\"fra\",\"name\":\"Guinéen\",\"isActive\":true},{\"code\":\"NNA\",\"langCode\":\"eng\",\"name\":\"Non-Native\",\"isActive\":true}]},\"errors\":null}";
		when(restTemplate.getForObject("https://0.0.0.0/individualtypes", ResponseWrapper.class))
				.thenReturn(mapper.readValue(response.getBytes(), ResponseWrapper.class));
	}

	@Bean
	public ObjectMapper objectMapper() {
		return mapper;
	}
}
