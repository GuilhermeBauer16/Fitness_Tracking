package integrationtests.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.GuilhermeBauer16.FitnessTracking.FitnessTrackingApplication;
import com.github.GuilhermeBauer16.FitnessTracking.config.TestConfigs;
import com.github.GuilhermeBauer16.FitnessTracking.enums.DifficultyLevel;
import com.github.GuilhermeBauer16.FitnessTracking.enums.ExerciseType;
import com.github.GuilhermeBauer16.FitnessTracking.enums.MuscleGroup;
import com.github.GuilhermeBauer16.FitnessTracking.enums.UserProfile;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.TokenVO;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.WorkoutExerciseVO;
import com.github.GuilhermeBauer16.FitnessTracking.request.LoginRequest;
import com.github.GuilhermeBauer16.FitnessTracking.utils.PaginatedResponse;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = FitnessTrackingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
class WorkoutExerciseControllerIntegrationTest extends AbstractionIntegrationTest {


    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static WorkoutExerciseVO workoutExerciseVO;


    private static final String USER_NAME = "john";
    private static final String EMAIL = "jonh@gmail.com";
    private static final String PASSWORD = "123456";
    private static final UserProfile USER_PROFILE = UserProfile.ADMIN;

    private static final String ID = "d8e7df81-2cd4-41a2-a005-62e6d8079716";
    private static final String NAME = "push-up";
    private static final String DESCRIPTION = "A push-up is a common calisthenics exercise beginning from the prone position.";
    private static final int CALORIES_BURNED = 50;
    private static final ExerciseType EXERCISE_TYPE = ExerciseType.STRENGTH;
    private static final String EQUIPMENT_NEEDED = "None";
    private static final DifficultyLevel DIFFICULTY_LEVEL = DifficultyLevel.BEGINNER;
    private static final Set<MuscleGroup> MUSCLE_GROUPS = new HashSet<>(Arrays.asList(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS));

    @BeforeAll
    public static void setup() {

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        workoutExerciseVO = new WorkoutExerciseVO(ID, NAME, DESCRIPTION, CALORIES_BURNED,
                EXERCISE_TYPE, EQUIPMENT_NEEDED, DIFFICULTY_LEVEL, MUSCLE_GROUPS);
    }

    @Test
    @Order(1)
    void signUp(){

        UserVO userVO = new UserVO(ID, USER_NAME, EMAIL, PASSWORD, USER_PROFILE);
        given()
                .basePath("/api/user/signUp")
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
                .setBasePath("/api/workoutExercise")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }


    @Test
    @Order(3)
    void givenWorkoutExerciseObject_when_CreateWorkoutExercise_ShouldReturnAWorkoutExerciseObject() throws IOException {
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(workoutExerciseVO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();


        workoutExerciseVO = objectMapper.readValue(content, WorkoutExerciseVO.class);

        Assertions.assertNotNull(workoutExerciseVO);
        Assertions.assertNotNull(workoutExerciseVO.getId());
        Assertions.assertTrue(workoutExerciseVO.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(NAME, workoutExerciseVO.getName());
        assertEquals(DESCRIPTION, workoutExerciseVO.getDescription());
        assertEquals(CALORIES_BURNED, workoutExerciseVO.getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, workoutExerciseVO.getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, workoutExerciseVO.getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, workoutExerciseVO.getMuscleGroups());
    }


    @Test
    @Order(4)
    void givenWorkoutExerciseObject_when_FindById_ShouldReturnAWorkoutExerciseObject() throws IOException {


        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", workoutExerciseVO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();


        WorkoutExerciseVO foundedWorkoutExerciseVO = objectMapper.readValue(content, WorkoutExerciseVO.class);


        Assertions.assertNotNull(foundedWorkoutExerciseVO);
        Assertions.assertNotNull(foundedWorkoutExerciseVO.getId());
        Assertions.assertTrue(foundedWorkoutExerciseVO.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(NAME, foundedWorkoutExerciseVO.getName());
        assertEquals(DESCRIPTION, foundedWorkoutExerciseVO.getDescription());
        assertEquals(CALORIES_BURNED, foundedWorkoutExerciseVO.getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, foundedWorkoutExerciseVO.getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, foundedWorkoutExerciseVO.getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, foundedWorkoutExerciseVO.getMuscleGroups());
    }

    @Test
    @Order(5)
    void givenWorkoutExerciseObject_when_findAllWorkoutExercise_ShouldReturnWorkoutExerciseList() throws JsonProcessingException {

        var content = given()
                .spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();


        PaginatedResponse<WorkoutExerciseVO> paginatedResponse = objectMapper.readValue(content, new TypeReference<>() {
        });
        List<WorkoutExerciseVO> workoutExerciseVOList = paginatedResponse.getContent();

        workoutExerciseVO = workoutExerciseVOList.getFirst();

        Assertions.assertNotNull(workoutExerciseVO);
        Assertions.assertNotNull(workoutExerciseVO.getId());
        Assertions.assertTrue(workoutExerciseVO.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(NAME, workoutExerciseVO.getName());
        assertEquals(DESCRIPTION, workoutExerciseVO.getDescription());
        assertEquals(CALORIES_BURNED, workoutExerciseVO.getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, workoutExerciseVO.getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, workoutExerciseVO.getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, workoutExerciseVO.getMuscleGroups());

    }

    @Test
    @Order(6)
    void givenWorkoutExerciseObject_when_UpdateWorkoutExercise_ShouldReturnAWorkoutExerciseObject() throws IOException {

        workoutExerciseVO.setName("squat");
        workoutExerciseVO.setCaloriesBurned(70);

        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(workoutExerciseVO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();


        workoutExerciseVO = objectMapper.readValue(content, WorkoutExerciseVO.class);

        Assertions.assertNotNull(workoutExerciseVO);
        Assertions.assertNotNull(workoutExerciseVO.getId());
        Assertions.assertTrue(workoutExerciseVO.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals("squat", workoutExerciseVO.getName());
        assertEquals(DESCRIPTION, workoutExerciseVO.getDescription());
        assertEquals(70, workoutExerciseVO.getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, workoutExerciseVO.getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, workoutExerciseVO.getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, workoutExerciseVO.getMuscleGroups());
    }


    @Order(7)
    @Test
    void givenWorkoutExercise_when_delete_ShouldReturnNoContent() {

        given()
                .spec(specification)
                .pathParam("id", workoutExerciseVO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);

    }
}


