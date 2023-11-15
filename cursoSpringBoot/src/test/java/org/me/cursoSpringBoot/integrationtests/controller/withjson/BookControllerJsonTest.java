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
import org.me.cursoSpringBoot.integrationtests.vo.BookVO;
import org.me.cursoSpringBoot.integrationtests.vo.PersonVO;
import org.me.cursoSpringBoot.integrationtests.vo.TokenVO;
import org.me.cursoSpringBoot.integrationtests.vo.wrappers.WrapperBookVO;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper mapper;
	private static BookVO bookVO;

	private static BookVO updatebookVO;
	private static BookVO[] booksVO;

	@BeforeAll
	public static void setup(){

		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		bookVO = new BookVO();
		updatebookVO = new BookVO();
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
	@Order(3)
	void testFindAll() throws JsonProcessingException {

		mockBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("limit", 2, "direction", "asc", "page", 0)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.when()
				.get()
				.then().statusCode(200)
				.extract()
				.body()
				.asString();

		WrapperBookVO createdBooks = mapper.readValue(content, WrapperBookVO.class);
		var books = createdBooks.getEmbedded().getBookVOS();
		BookVO bookVO1 = books.get(0);

		assertEquals(3, bookVO1.getId());
		assertEquals("Carl Sagan", bookVO1.getTitle());
		assertEquals("Pálido ponto azul", bookVO1.getAuthor());
	}

	@Test
	@Order(4)
	void testUpdate() throws JsonProcessingException {

		mockUpdateBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.body(updatebookVO)
				.when()
				.put()
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
		assertEquals("Pálido Ponto Azul", createdBook.getTitle());
		assertEquals("Carl Sagan", createdBook.getAuthor());
	}

	@Test
	@Order(5)
	void testDelete() throws JsonProcessingException {

		given().spec(specification)
		.contentType(TestConfigs.CONTENT_TYPE_JSON)
		.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
		.pathParam("id", bookVO.getId())
		.when()
		.delete("{id}")
		.then().statusCode(204);
	}

	@Test
	@Order(6)
	void testDeleteWithoutToken() throws JsonProcessingException {

		 RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		given().spec(specificationWithoutToken)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.pathParam("id", bookVO.getId())
				.when()
				.delete("{id}")
				.then().statusCode(403);
	}

	@Test
	@Order(7)
	void testHateoas() throws JsonProcessingException {

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

		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/book/v1/3\"}}"));
		assertTrue(content.contains("\"page\":{\"size\":6,\"totalElements\":3,\"totalPages\":1,\"number\":0}}"));
	}

	private void mockBook() {

		bookVO.setTitle("Darwin sem frescuras");
		bookVO.setAuthor("Reinaldo José Lopez e Pirula");
	}

	private void mockUpdateBook() {

		updatebookVO.setId(4);
		updatebookVO.setTitle("Pálido Ponto Azul");
		updatebookVO.setAuthor("Carl Sagan");
	}
}
