package integrationtests.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.GuilhermeBauer16.FitnessTracking.FitnessTrackingApplication;
import com.github.GuilhermeBauer16.FitnessTracking.config.TestConfigs;
import com.github.GuilhermeBauer16.FitnessTracking.enums.UserProfile;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.TokenVO;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;
import com.github.GuilhermeBauer16.FitnessTracking.request.LoginRequest;
import com.github.GuilhermeBauer16.FitnessTracking.response.UserResponse;
import integrationtests.testContainers.AbstractionIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = FitnessTrackingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class UserControllerTest extends AbstractionIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static UserResponse userResponse;

    private static final String ID = "d8e7df81-2cd4-41a2-a005-62e6d8079716";
    private static final String USER_NAME = "john";
    private static final String EMAIL = "JohnWick@gmail.com";
    private static final String PASSWORD = "123456";
    private static final UserProfile USER_PROFILE = UserProfile.ADMIN;

    @BeforeAll
    public static void setup() {

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        userResponse = new UserResponse(USER_NAME, EMAIL);
    }

    @Test
    @Order(1)
    void signUp() throws JsonProcessingException {

        UserVO userVO = new UserVO(ID, USER_NAME, EMAIL, PASSWORD, USER_PROFILE);
        String content = given()
                .basePath("/api/user/signIn")
                .port(8889)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(userVO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        UserResponse userResponseRecovered = objectMapper.readValue(content, UserResponse.class);

        Assertions.assertNotNull(userResponseRecovered);
        assertEquals(USER_NAME, userResponseRecovered.getName());
        assertEquals(EMAIL, userResponseRecovered.getEmail());

    }

    @Test
    @Order(2)
    void authorization() {

        LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD);
        var accessToken = given()
                .basePath("/api/user/login")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(loginRequest)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class)
                .getAccessToken();


        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBaseUri("http://localhost:" + TestConfigs.SERVER_PORT)
                .setBasePath("/api/user")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(3)
    void givenWEmail_when_FindByEmail_ShouldReturnAUserResponseObject() throws IOException {

        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("email", userResponse.getEmail())
                .when()
                .get("{email}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        UserResponse userResponseRecovered = objectMapper.readValue(content, UserResponse.class);

        assertEquals(USER_NAME, userResponseRecovered.getName());
        assertEquals(EMAIL, userResponseRecovered.getEmail());
    }

    @Order(4)
    @Test
    void givenEmail_when_delete_ShouldReturnNoContent() {

        given()
                .spec(specification)
                .pathParam("email", userResponse.getEmail())
                .when()
                .delete("{email}")
                .then()
                .statusCode(204);

    }

}
