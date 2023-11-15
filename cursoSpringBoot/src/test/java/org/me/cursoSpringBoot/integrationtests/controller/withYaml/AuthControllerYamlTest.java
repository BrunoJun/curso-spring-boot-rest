package org.me.cursoSpringBoot.integrationtests.controller.withYaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.me.cursoSpringBoot.configs.TestConfigs;
import org.me.cursoSpringBoot.integrationtests.testcontainers.AbstractIntegrationTest;
import org.me.cursoSpringBoot.integrationtests.vo.AccountCredentialsVO;
import org.me.cursoSpringBoot.integrationtests.vo.TokenVO;
import org.me.cursoSpringBoot.unitTests.mapper.JsonToYamlConverter;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYamlTest extends AbstractIntegrationTest {

    private static JsonToYamlConverter jsonToYamlConverter;
    private static TokenVO tokenVO;

    @BeforeAll
    public static void setup(){

        jsonToYamlConverter = new JsonToYamlConverter();
    }

    @Test
    @Order(1)
    void testSignin() throws JsonProcessingException {

        AccountCredentialsVO accountCredentialsVO = new AccountCredentialsVO("leandro", "admin123");

        tokenVO = given()
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .body(accountCredentialsVO, jsonToYamlConverter)
                .when()
                .post()
                .then().statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class, jsonToYamlConverter);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }

    @Test
    @Order(2)
    void testRefresh() throws JsonProcessingException {

        AccountCredentialsVO accountCredentialsVO = new AccountCredentialsVO("leandro", "admin123");

        var newTokenVO = given()
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .pathParam("username", tokenVO.getUsername())
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                .when()
                .put("{username}")
                .then().statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class, jsonToYamlConverter);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }
}
