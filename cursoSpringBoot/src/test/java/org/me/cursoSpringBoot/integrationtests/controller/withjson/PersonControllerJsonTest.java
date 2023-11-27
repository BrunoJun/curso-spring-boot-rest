package org.me.cursoSpringBoot.integrationtests.controller.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.me.cursoSpringBoot.configs.TestConfigs;
import org.me.cursoSpringBoot.integrationtests.testcontainers.AbstractIntegrationTest;
import org.me.cursoSpringBoot.integrationtests.vo.AccountCredentialsVO;
import org.me.cursoSpringBoot.integrationtests.vo.PersonVO;
import org.me.cursoSpringBoot.integrationtests.vo.TokenVO;
import org.me.cursoSpringBoot.integrationtests.vo.wrappers.WrapperPersonVO;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper mapper;
	private static PersonVO personVO;

	private static PersonVO updatePersonVO;
	private static PersonVO[] peopleVO;

	@BeforeAll
	public static void setup(){

		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		personVO = new PersonVO();
		updatePersonVO = new PersonVO();
	}

	@Test
	@Order(0)
	void authorization() throws JsonProcessingException {

		AccountCredentialsVO accountCredentialsVO = new AccountCredentialsVO("bruno", "admin123");

		var accessToken = given()
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(accountCredentialsVO)
				.when()
				.post()
				.then().statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class).getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	void testCreate() throws JsonProcessingException {

		mockPerson();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.body(personVO)
				.when()
				.post()
				.then().statusCode(200)
				.extract()
				.body()
				.asString();

		PersonVO createdPerson = mapper.readValue(content, PersonVO.class);
		personVO = createdPerson;

		assertTrue(createdPerson.getId() > 0);
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());
		assertEquals("Karim", createdPerson.getFirstName());
		assertEquals("Benzema", createdPerson.getLastName());
		assertEquals("Arabia", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
	}

	@Test
	@Order(2)
	void testDisablePersonById() throws JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.pathParam("id", personVO.getId())
				.when()
				.patch("{id}")
				.then().statusCode(200)
				.extract()
				.body()
				.asString();

		PersonVO createdPerson = mapper.readValue(content, PersonVO.class);
		personVO = createdPerson;

		assertTrue(createdPerson.getId() > 0);
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		assertFalse(createdPerson.getEnabled());
		assertEquals("Karim", createdPerson.getFirstName());
		assertEquals("Benzema", createdPerson.getLastName());
		assertEquals("Arabia", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
	}

	@Test
	@Order(3)
	void testFindById() throws JsonProcessingException {

		mockPerson();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.pathParam("id", personVO.getId())
				.when()
				.get("{id}")
				.then().statusCode(200)
				.extract()
				.body()
				.asString();

		PersonVO createdPerson = mapper.readValue(content, PersonVO.class);
		personVO = createdPerson;

		assertTrue(createdPerson.getId() > 0);
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		assertFalse(createdPerson.getEnabled());
		assertEquals("Karim", createdPerson.getFirstName());
		assertEquals("Benzema", createdPerson.getLastName());
		assertEquals("Arabia", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
	}

	@Test
	@Order(4)
	void testFindAll() throws JsonProcessingException {

		mockPerson();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("limit", 10, "direction", "asc", "page", 10)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.when()
				.get()
				.then().statusCode(200)
				.extract()
				.body()
				.asString();

		WrapperPersonVO wrapper = mapper.readValue(content, WrapperPersonVO.class);
		var people = wrapper.getEmbedded().getPersonVOS();
		PersonVO person = people.get(0);

		assertEquals(193, person.getId());
		assertEquals("Benedick", person.getFirstName());
		assertEquals("Gwynn", person.getLastName());
		assertEquals("9 Maple Circle", person.getAddress());
		assertEquals("Male", person.getGender());
	}

	@Test
	@Order(5)
	void testFindByName() throws JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("firstName", "br")
				.queryParams("limit", 6, "direction", "asc", "page", 0)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.when()
				.get("findPersonsByName/{firstName}")
				.then().statusCode(200)
				.extract()
				.body()
				.asString();

		WrapperPersonVO wrapper = mapper.readValue(content, WrapperPersonVO.class);
		var people = wrapper.getEmbedded().getPersonVOS();
		PersonVO person = people.get(0);

		assertEquals(331, person.getId());
		assertEquals("Albrecht", person.getFirstName());
		assertEquals("Tassell", person.getLastName());
		assertEquals("638 Northwestern Plaza", person.getAddress());
		assertEquals("Male", person.getGender());
	}

	@Test
	@Order(6)
	void testUpdate() throws JsonProcessingException {

		mockUpdatePerson();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.body(updatePersonVO)
				.when()
				.put()
				.then().statusCode(200)
				.extract()
				.body()
				.asString();

		PersonVO createdPerson = mapper.readValue(content, PersonVO.class);
		personVO = createdPerson;

		assertTrue(createdPerson.getId() > 0);
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());

		assertEquals("Heung-Min", createdPerson.getFirstName());
		assertEquals("Son", createdPerson.getLastName());
		assertEquals("England", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());
	}

	@Test
	@Order(7)
	void testDelete() throws JsonProcessingException {

		given().spec(specification)
		.contentType(TestConfigs.CONTENT_TYPE_JSON)
		.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
		.pathParam("id", personVO.getId())
		.when()
		.delete("{id}")
		.then().statusCode(204);
	}

	@Test
	@Order(8)
	void testHateos() throws JsonProcessingException {

		mockPerson();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("limit", 6, "direction", "asc", "page", 0)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.when()
				.get()
				.then().statusCode(200)
				.extract()
				.body()
				.asString();

		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/219\"}}"));

		assertTrue(content.contains("\"page\":{\"size\":6,\"totalElements\":1000,\"totalPages\":167,\"number\":0}"));

		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/person/v1?limit=6&direction=asc&page=166&size=6&sort=firstName,asc\"}}"));
		assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/person/v1?limit=6&direction=asc&page=1&size=6&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"first\":{\"href\":\"http://localhost:8888/api/person/v1?limit=6&direction=asc&page=0&size=6&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/person/v1?page=0&limit=6&direction=asc\"}"));
	}

	@Test
	@Order(9)
	void testDeleteWithoutToken() throws JsonProcessingException {

		 RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		given().spec(specificationWithoutToken)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.pathParam("id", personVO.getId())
				.when()
				.delete("{id}")
				.then().statusCode(403);
	}

	private void mockPerson() {

		personVO.setFirstName("Karim");
		personVO.setLastName("Benzema");
		personVO.setAddress("Arabia");
		personVO.setGender("Male");
		personVO.setEnabled(true);
	}

	private void mockUpdatePerson() {

		updatePersonVO.setId(1);
		updatePersonVO.setFirstName("Heung-Min");
		updatePersonVO.setLastName("Son");
		updatePersonVO.setAddress("England");
		updatePersonVO.setGender("Male");
		updatePersonVO.setEnabled(true);
	}
}
