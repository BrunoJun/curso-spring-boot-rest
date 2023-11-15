package org.me.cursoSpringBoot.integrationtests.controller.cors.withJson;

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
import org.me.cursoSpringBoot.integrationtests.vo.BookVO;
import org.me.cursoSpringBoot.integrationtests.vo.PersonVO;
import org.me.cursoSpringBoot.integrationtests.vo.TokenVO;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerCorsJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper mapper;
	private static BookVO bookVO;

	@BeforeAll
	public static void setup(){

		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		bookVO = new BookVO();
	}

	@Test
	@Order(0)
	void authorization() throws JsonProcessingException {

		AccountCredentialsVO accountCredentialsVO = new AccountCredentialsVO("leandro", "admin123");

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
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	void testCreate() throws JsonProcessingException {

		mockBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.body(bookVO)
				.when()
				.post()
				.then().statusCode(200)
				.extract()
				.body()
				.asString();

		BookVO createdBook = mapper.readValue(content, BookVO.class);
		bookVO = createdBook;

		assertTrue(createdBook.getId() > 0);
		assertNotNull(createdBook.getId());
		assertNotNull(createdBook.getTitle());
		assertNotNull(createdBook.getAuthor());
		assertEquals("Darwin sem frescuras", createdBook.getTitle());
		assertEquals("Reinaldo José Lopez e Pirula", createdBook.getAuthor());
	}

	@Test
	@Order(2)
	void testCreateWithWrongOrigin() throws JsonProcessingException {

		mockBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
				.body(bookVO)
				.when()
				.post()
				.then().statusCode(403)
				.extract()
				.body()
				.asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(3)
	void testFindById() throws JsonProcessingException {

		mockBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.pathParam("id", bookVO.getId())
				.when()
				.get("{id}")
				.then().statusCode(200)
				.extract()
				.body()
				.asString();

		BookVO createdBook = mapper.readValue(content, BookVO.class);
		bookVO = createdBook;

		assertTrue(createdBook.getId() > 0);
		assertNotNull(createdBook.getId());
		assertNotNull(createdBook.getTitle());
		assertNotNull(createdBook.getAuthor());
		assertEquals("Darwin sem frescuras", createdBook.getTitle());
		assertEquals("Reinaldo José Lopez e Pirula", createdBook.getAuthor());
	}
	@Test
	@Order(7)
	void testFindByIdWithWrongOrigin() throws JsonProcessingException {

		mockBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
				.pathParam("id", bookVO.getId())
				.when()
				.get("{id}")
				.then().statusCode(403)
				.extract()
				.body()
				.asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	private void mockBook() {

		bookVO.setTitle("Darwin sem frescuras");
		bookVO.setAuthor("Reinaldo José Lopez e Pirula");
	}
}
