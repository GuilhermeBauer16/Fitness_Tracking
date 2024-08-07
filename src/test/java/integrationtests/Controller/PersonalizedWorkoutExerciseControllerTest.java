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
import com.github.GuilhermeBauer16.FitnessTracking.model.WorkoutExerciseEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.PersonalizedWorkoutTrainingVO;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.TokenVO;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;
import com.github.GuilhermeBauer16.FitnessTracking.repository.UserRepository;
import com.github.GuilhermeBauer16.FitnessTracking.repository.WorkoutExerciseRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
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
public class PersonalizedWorkoutExerciseControllerTest extends AbstractionIntegrationTest {

    @Autowired
    UserRepository userRepository;

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonalizedWorkoutTrainingVO personalizedWorkoutTrainingVO;

    private static final String ID = "d8e7df81-2cd4-41a2-a005-62e6d8079716";

    private static final Integer REPETITIONS = 10;
    private static final Integer SERIES = 4;
    private static final Double WEIGHT = 40d;

    private static final String USER_NAME = "john";
    private static final String EMAIL = "jonhBee@gmail.com";
    private static final String PASSWORD = "123456";
    private static final UserProfile USER_PROFILE = UserProfile.ADMIN;

    private static final String NAME = "push-up";
    private static final String DESCRIPTION = "A push-up is a common calisthenics exercise beginning from the prone position.";
    private static final int CALORIES_BURNED = 50;
    private static final ExerciseType EXERCISE_TYPE = ExerciseType.STRENGTH;
    private static final String EQUIPMENT_NEEDED = "None";
    private static final DifficultyLevel DIFFICULTY_LEVEL = DifficultyLevel.BEGINNER;
    private static final Set<MuscleGroup> MUSCLE_GROUPS = new HashSet<>(Arrays.asList(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS));

    @BeforeAll
    public static void SetUp(@Autowired WorkoutExerciseRepository workoutExerciseRepository) {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


        WorkoutExerciseEntity workoutExerciseEntity = new WorkoutExerciseEntity(ID, NAME, DESCRIPTION, CALORIES_BURNED,
                EXERCISE_TYPE, EQUIPMENT_NEEDED, DIFFICULTY_LEVEL, MUSCLE_GROUPS);

        workoutExerciseRepository.save(workoutExerciseEntity);

        personalizedWorkoutTrainingVO = new PersonalizedWorkoutTrainingVO(ID, workoutExerciseEntity, REPETITIONS, SERIES, WEIGHT);
    }

    @Test
    @Order(1)
    void signUp() {

        UserVO userVO = new UserVO(ID, USER_NAME, EMAIL, PASSWORD, USER_PROFILE);
        given()
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
                .setBasePath("/api/personalizedWorkout")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }



    @Test
    @Order(3)
    void givenPersonalizedWorkoutTrainingObject_when_CreatePersonalizedWorkoutTraining_ShouldReturnAPersonalizedWorkoutTrainingObject() throws IOException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(personalizedWorkoutTrainingVO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();


        personalizedWorkoutTrainingVO = objectMapper.readValue(content, PersonalizedWorkoutTrainingVO.class);

        Assertions.assertNotNull(personalizedWorkoutTrainingVO);
        Assertions.assertNotNull(personalizedWorkoutTrainingVO.getWorkoutExerciseEntity());
        Assertions.assertNotNull(personalizedWorkoutTrainingVO.getId());
        Assertions.assertTrue(personalizedWorkoutTrainingVO.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(REPETITIONS, personalizedWorkoutTrainingVO.getRepetitions());
        assertEquals(WEIGHT, personalizedWorkoutTrainingVO.getWeight());
        assertEquals(SERIES, personalizedWorkoutTrainingVO.getSeries());

        assertEquals(NAME, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getMuscleGroups());
    }

    @Test
    @Order(4)
    void givenPersonalizedWorkoutTrainingObject_when_FindPersonalizedWorkoutTrainingById_ShouldReturnAPersonalizedWorkoutTrainingObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .pathParam("id", personalizedWorkoutTrainingVO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        personalizedWorkoutTrainingVO = objectMapper.readValue(content, PersonalizedWorkoutTrainingVO.class);

        Assertions.assertNotNull(personalizedWorkoutTrainingVO);
        Assertions.assertNotNull(personalizedWorkoutTrainingVO.getWorkoutExerciseEntity());
        Assertions.assertNotNull(personalizedWorkoutTrainingVO.getId());
        Assertions.assertTrue(personalizedWorkoutTrainingVO.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(REPETITIONS, personalizedWorkoutTrainingVO.getRepetitions());
        assertEquals(WEIGHT, personalizedWorkoutTrainingVO.getWeight());
        assertEquals(SERIES, personalizedWorkoutTrainingVO.getSeries());

        assertEquals(NAME, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getMuscleGroups());


    }

    @Test
    @Order(5)
    void givenPersonalizedWorkoutTrainingList_when_FindAllPersonalizedWorkoutTraining_ShouldReturnAPersonalizedWorkoutTrainingList() throws JsonProcessingException {

        var content = given().spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PaginatedResponse<PersonalizedWorkoutTrainingVO> paginatedResponse =
                objectMapper.readValue(content, new TypeReference<PaginatedResponse<PersonalizedWorkoutTrainingVO>>() {
                });

        List<PersonalizedWorkoutTrainingVO> personalizedWorkoutTrainingVOList = paginatedResponse.getContent();
        PersonalizedWorkoutTrainingVO personalizedWorkoutTrainingVOListFirst = personalizedWorkoutTrainingVOList.getFirst();

        Assertions.assertNotNull(personalizedWorkoutTrainingVOListFirst);
        Assertions.assertNotNull(personalizedWorkoutTrainingVOListFirst.getWorkoutExerciseEntity());
        Assertions.assertNotNull(personalizedWorkoutTrainingVOListFirst.getId());

        Assertions.assertTrue(personalizedWorkoutTrainingVOListFirst.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(REPETITIONS, personalizedWorkoutTrainingVOListFirst.getRepetitions());
        assertEquals(WEIGHT, personalizedWorkoutTrainingVOListFirst.getWeight());
        assertEquals(SERIES, personalizedWorkoutTrainingVOListFirst.getSeries());

        assertEquals(NAME, personalizedWorkoutTrainingVOListFirst.getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, personalizedWorkoutTrainingVOListFirst.getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, personalizedWorkoutTrainingVOListFirst.getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, personalizedWorkoutTrainingVOListFirst.getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, personalizedWorkoutTrainingVOListFirst.getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, personalizedWorkoutTrainingVOListFirst.getWorkoutExerciseEntity().getMuscleGroups());


    }

    @Test
    @Order(6)
    void GivenPersonalizedWorkoutTrainingObject_when_FindPersonalizedWorkoutTrainingByMuscle_ShouldReturnAPersonalizedWorkoutTrainingList() throws IOException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(personalizedWorkoutTrainingVO)
                .when()
                .get("/findByMuscleGroup")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonalizedWorkoutTrainingVO[] createdPersonalizedWorkoutTrainingVO =
                objectMapper.readValue(content, PersonalizedWorkoutTrainingVO[].class);

        List<PersonalizedWorkoutTrainingVO> personalizedWorkoutTrainingVOList = Arrays.asList(createdPersonalizedWorkoutTrainingVO);

        personalizedWorkoutTrainingVO = personalizedWorkoutTrainingVOList.getFirst();

        Assertions.assertNotNull(personalizedWorkoutTrainingVO);
        Assertions.assertNotNull(personalizedWorkoutTrainingVO.getWorkoutExerciseEntity());
        Assertions.assertNotNull(personalizedWorkoutTrainingVO.getId());
        Assertions.assertTrue(personalizedWorkoutTrainingVO.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(REPETITIONS, personalizedWorkoutTrainingVO.getRepetitions());
        assertEquals(WEIGHT, personalizedWorkoutTrainingVO.getWeight());
        assertEquals(SERIES, personalizedWorkoutTrainingVO.getSeries());

        assertEquals(NAME, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getMuscleGroups());
    }

    @Test
    @Order(7)
    void GivenPersonalizedWorkoutTrainingObject_when_FindPersonalizedWorkoutTrainingByDifficultLevel_ShouldReturnAPersonalizedWorkoutTrainingList() throws IOException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(personalizedWorkoutTrainingVO)
                .when()
                .get("/findByDifficultLevel")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonalizedWorkoutTrainingVO[] createdPersonalizedWorkoutTrainingVO =
                objectMapper.readValue(content, PersonalizedWorkoutTrainingVO[].class);

        List<PersonalizedWorkoutTrainingVO> personalizedWorkoutTrainingVOList = Arrays.asList(createdPersonalizedWorkoutTrainingVO);

        personalizedWorkoutTrainingVO = personalizedWorkoutTrainingVOList.getFirst();

        Assertions.assertNotNull(personalizedWorkoutTrainingVO);
        Assertions.assertNotNull(personalizedWorkoutTrainingVO.getWorkoutExerciseEntity());
        Assertions.assertNotNull(personalizedWorkoutTrainingVO.getId());
        Assertions.assertTrue(personalizedWorkoutTrainingVO.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(REPETITIONS, personalizedWorkoutTrainingVO.getRepetitions());
        assertEquals(WEIGHT, personalizedWorkoutTrainingVO.getWeight());
        assertEquals(SERIES, personalizedWorkoutTrainingVO.getSeries());

        assertEquals(NAME, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getMuscleGroups());
    }

    @Test
    @Order(8)
    void GivenPersonalizedWorkoutTrainingObject_when_FindPersonalizedWorkoutTrainingByName_ShouldReturnAPersonalizedWorkoutTrainingList() throws IOException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(personalizedWorkoutTrainingVO)
                .when()
                .get("/findByName")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonalizedWorkoutTrainingVO[] createdPersonalizedWorkoutTrainingVO =
                objectMapper.readValue(content, PersonalizedWorkoutTrainingVO[].class);

        List<PersonalizedWorkoutTrainingVO> personalizedWorkoutTrainingVOList = Arrays.asList(createdPersonalizedWorkoutTrainingVO);

        personalizedWorkoutTrainingVO = personalizedWorkoutTrainingVOList.getFirst();

        Assertions.assertNotNull(personalizedWorkoutTrainingVO);
        Assertions.assertNotNull(personalizedWorkoutTrainingVO.getWorkoutExerciseEntity());
        Assertions.assertNotNull(personalizedWorkoutTrainingVO.getId());
        Assertions.assertTrue(personalizedWorkoutTrainingVO.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(REPETITIONS, personalizedWorkoutTrainingVO.getRepetitions());
        assertEquals(WEIGHT, personalizedWorkoutTrainingVO.getWeight());
        assertEquals(SERIES, personalizedWorkoutTrainingVO.getSeries());

        assertEquals(NAME, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getMuscleGroups());

    }

    @Test
    @Order(9)
    void GivenPersonalizedWorkoutTrainingObject_when_UpdatePersonalizedWorkoutTraining_ShouldReturnAPersonalizedWorkoutTrainingObject() throws IOException {

        personalizedWorkoutTrainingVO.setRepetitions(50);
        personalizedWorkoutTrainingVO.setWeight(20D);

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(personalizedWorkoutTrainingVO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        personalizedWorkoutTrainingVO = objectMapper.readValue(content, PersonalizedWorkoutTrainingVO.class);


        Assertions.assertNotNull(personalizedWorkoutTrainingVO);
        Assertions.assertNotNull(personalizedWorkoutTrainingVO.getWorkoutExerciseEntity());
        Assertions.assertNotNull(personalizedWorkoutTrainingVO.getId());
        Assertions.assertTrue(personalizedWorkoutTrainingVO.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(50, personalizedWorkoutTrainingVO.getRepetitions());
        assertEquals(20D, personalizedWorkoutTrainingVO.getWeight());
        assertEquals(SERIES, personalizedWorkoutTrainingVO.getSeries());

        assertEquals(NAME, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getMuscleGroups());
    }

    @Order(10)
    @Test
    void givenPersonalizedWorkoutExercise_when_delete_ShouldReturnNoContent() {

        given().spec(specification)
                .pathParam("id", personalizedWorkoutTrainingVO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);

    }


}
