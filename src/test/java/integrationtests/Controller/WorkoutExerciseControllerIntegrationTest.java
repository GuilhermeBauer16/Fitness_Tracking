package integrationtests.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.GuilhermeBauer16.FitnessTracking.FitnessTrackingApplication;
import com.github.GuilhermeBauer16.FitnessTracking.config.TestConfigs;
import com.github.GuilhermeBauer16.FitnessTracking.enums.DifficultyLevel;
import com.github.GuilhermeBauer16.FitnessTracking.enums.ExerciseType;
import com.github.GuilhermeBauer16.FitnessTracking.enums.MuscleGroup;
import com.github.GuilhermeBauer16.FitnessTracking.utils.PaginatedResponse;
import com.github.GuilhermeBauer16.FitnessTracking.vo.WorkoutExerciseVO;
import integrationtests.testContainers.AbstractionIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = FitnessTrackingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
class WorkoutExerciseControllerIntegrationTest extends AbstractionIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static WorkoutExerciseVO workoutExerciseVO;

    private final String WORKOUT_EXERCISE_NOT_FOUND_MESSAGE = "Can not be find that Workout exercise!";

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

        specification = new RequestSpecBuilder()
                .setBaseUri("http://localhost:8889")
                .setBasePath("/workoutExercise")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        workoutExerciseVO = new WorkoutExerciseVO(ID, NAME, DESCRIPTION, CALORIES_BURNED,
                EXERCISE_TYPE, EQUIPMENT_NEEDED, DIFFICULTY_LEVEL, MUSCLE_GROUPS);
    }

    @Test
    @Order(1)
    void integrationTestGivenWorkoutExerciseObject_when_CreateWorkoutExercise_ShouldReturnAWorkoutExerciseObject() throws IOException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(workoutExerciseVO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        WorkoutExerciseVO createdWorkoutExerciseVO = objectMapper.readValue(content, WorkoutExerciseVO.class);
        workoutExerciseVO = createdWorkoutExerciseVO;

        Assertions.assertNotNull(workoutExerciseVO);
        assertNotNull(workoutExerciseVO.getId());
        assertTrue(workoutExerciseVO.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(NAME, workoutExerciseVO.getName());
        assertEquals(DESCRIPTION, workoutExerciseVO.getDescription());
        assertEquals(CALORIES_BURNED, workoutExerciseVO.getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, workoutExerciseVO.getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, workoutExerciseVO.getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, workoutExerciseVO.getMuscleGroups());
    }

    @Test
    @Order(2)
    void integrationTestGivenWorkoutExerciseObject_when_UpdateWorkoutExercise_ShouldReturnAWorkoutExerciseObject() throws IOException {

        workoutExerciseVO.setName("squat");
        workoutExerciseVO.setCaloriesBurned(70);

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(workoutExerciseVO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();


        WorkoutExerciseVO updatedWorkoutExerciseVO = objectMapper.readValue(content, WorkoutExerciseVO.class);
        workoutExerciseVO = updatedWorkoutExerciseVO;

        assertNotNull(updatedWorkoutExerciseVO);
        assertNotNull(updatedWorkoutExerciseVO.getId());
        assertTrue(updatedWorkoutExerciseVO.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals("squat", updatedWorkoutExerciseVO.getName());
        assertEquals(DESCRIPTION, updatedWorkoutExerciseVO.getDescription()); // Assuming description remains unchanged
        assertEquals(70, updatedWorkoutExerciseVO.getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, updatedWorkoutExerciseVO.getExerciseType()); // Assuming exercise type remains unchanged
        assertEquals(DIFFICULTY_LEVEL, updatedWorkoutExerciseVO.getDifficultyLevel()); // Assuming difficulty level remains unchanged
        assertEquals(MUSCLE_GROUPS, updatedWorkoutExerciseVO.getMuscleGroups()); // Assuming muscle groups remain unchanged
    }

    @Test
    @Order(3)
    void integrationTestGivenWorkoutExerciseObject_when_FindById_ShouldReturnAWorkoutExerciseObject() throws IOException {


        var content = given().spec(specification)
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


        assertNotNull(foundedWorkoutExerciseVO);
        assertNotNull(foundedWorkoutExerciseVO.getId());
        assertTrue(foundedWorkoutExerciseVO.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals("squat", foundedWorkoutExerciseVO.getName());
        assertEquals(DESCRIPTION, foundedWorkoutExerciseVO.getDescription()); // Assuming description remains unchanged
        assertEquals(70, foundedWorkoutExerciseVO.getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, foundedWorkoutExerciseVO.getExerciseType()); // Assuming exercise type remains unchanged
        assertEquals(DIFFICULTY_LEVEL, foundedWorkoutExerciseVO.getDifficultyLevel()); // Assuming difficulty level remains unchanged
        assertEquals(MUSCLE_GROUPS, foundedWorkoutExerciseVO.getMuscleGroups()); // Assuming muscle groups remain unchanged
    }

    @Test
    @Order(4)
    void integrationTestGivenPersonObject_when_findAll_ShouldReturnAPeopleList() throws JsonMappingException, JsonProcessingException {
        String ANOTHER_DESCRIPTION = "A squat is a strength exercise where the trainee lowers their hips from a standing position and then stands back up.";

        WorkoutExerciseVO anotherWorkoutExercise = new WorkoutExerciseVO(
                UUID.randomUUID().toString(), "squat", ANOTHER_DESCRIPTION,
                70,
                ExerciseType.STRENGTH,
                "None",
                DifficultyLevel.INTERMEDIATE, new HashSet<>(Arrays.asList(MuscleGroup.LEGS, MuscleGroup.GLUTES)));

        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(anotherWorkoutExercise)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        var content = given().spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        System.out.println(content);

        PaginatedResponse<WorkoutExerciseVO> paginatedResponse = objectMapper.readValue(content, new TypeReference<PaginatedResponse<WorkoutExerciseVO>>() {});
        List<WorkoutExerciseVO> workoutExerciseVOList = paginatedResponse.getContent();

        WorkoutExerciseVO workoutExerciseVO1 = workoutExerciseVOList.stream()
                .filter(we -> we.getDescription().equals(DESCRIPTION))
                .findFirst()
                .orElseThrow(() -> new AssertionError(WORKOUT_EXERCISE_NOT_FOUND_MESSAGE));

        assertNotNull(workoutExerciseVO1);
        assertNotNull(workoutExerciseVO1.getId());
        assertTrue(workoutExerciseVO1.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals("squat", workoutExerciseVO1.getName());
        assertEquals(DESCRIPTION, workoutExerciseVO1.getDescription()); // Assuming description remains unchanged
        assertEquals(70, workoutExerciseVO1.getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, workoutExerciseVO1.getExerciseType()); // Assuming exercise type remains unchanged
        assertEquals(DIFFICULTY_LEVEL, workoutExerciseVO1.getDifficultyLevel()); // Assuming difficulty level remains unchanged
        assertEquals(new HashSet<>(Arrays.asList(MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS, MuscleGroup.CHEST)), workoutExerciseVO1.getMuscleGroups());

        WorkoutExerciseVO workoutExerciseVO2 = workoutExerciseVOList.stream()
                .filter(we -> we.getDescription().equals(ANOTHER_DESCRIPTION))
                .findFirst()
                .orElseThrow(() -> new AssertionError(WORKOUT_EXERCISE_NOT_FOUND_MESSAGE));

        assertNotNull(workoutExerciseVO2);
        assertNotNull(workoutExerciseVO2.getId());
        assertTrue(workoutExerciseVO2.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals("squat", workoutExerciseVO2.getName());
        assertEquals(ANOTHER_DESCRIPTION, workoutExerciseVO2.getDescription()); // Assuming description remains unchanged
        assertEquals(70, workoutExerciseVO2.getCaloriesBurned());
        assertEquals(ExerciseType.STRENGTH, workoutExerciseVO2.getExerciseType()); // Assuming exercise type remains unchanged
        assertEquals(DifficultyLevel.INTERMEDIATE, workoutExerciseVO2.getDifficultyLevel()); // Assuming difficulty level remains unchanged
        assertEquals(new HashSet<>(Arrays.asList(MuscleGroup.LEGS, MuscleGroup.GLUTES)), workoutExerciseVO2.getMuscleGroups());
    }



    @Order(5)
    @Test
    void integrationTestGivenWorkoutExercise_when_delete_ShouldReturnNoContent() {

        given().spec(specification)
                .pathParam("id", workoutExerciseVO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);

    }
}


