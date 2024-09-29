# Fitness Tracking  
    
[![Deploy in Docker Hub](https://github.com/GuilhermeBauer16/Fitness_Tracking/actions/workflows/Deploy-In-DockerHub.yml/badge.svg)](https://github.com/GuilhermeBauer16/Fitness_Tracking/actions/workflows/Deploy-In-DockerHub.yml) 
[![CI-Pull-Request](https://github.com/GuilhermeBauer16/Fitness_Tracking/actions/workflows/CI-Pull-Request.yml/badge.svg)](https://github.com/GuilhermeBauer16/Fitness_Tracking/actions/workflows/CI-Pull-Request.yml)

## Objective  

Users will log in and use Fitness Tracking functions to personalize exercises by adjusting the weight, repetitions, and sets. Before using the app, users need to sign up or log in.  

## Working Example  

![Screenshot from 2024-08-08 19-16-00](https://github.com/user-attachments/assets/f44b39c3-f804-45ea-84be-36832629d856)

## Sending  

![Screenshot from 2024-08-08 19-18-27](https://github.com/user-attachments/assets/4c635e45-a7bd-450e-b6a4-77ba2e96b3a5)

## Response  

![Screenshot from 2024-08-08 19-19-11](https://github.com/user-attachments/assets/ec419e37-ee13-4782-8368-05f56a50b9e9)

## Learnings  

In this project, I learned how to create a login system using Spring Security and JWT (JSON Web Tokens) to structure the login system. I also learned and implemented unit tests using Mockito and JUnit. To create the integration tests, I set up a MySQL container so the tests are close to a production environment. I learned how to implement Docker Compose and set up CI/CD in the GitHub repository for my project. After each update, the changes are reflected in the Docker Hub repository, generating a new latest version.

## Functionalities

### User 
* **Login** with Spring Security authentication.
* ```dotlogin
    {
    "email": "john@hotmail.com", // Ensure that this email is already registered
    "password": "123456"}
    ```

* Creation of User
* ```dotuser
  {
  "name": "john",
  "email": "john@hotmail.com",
  "password": "123456",
  "userProfile": "admin"}
    ```

* Find user by email.
  
* Delete user.


### Workout Exercise
* Addition of workout exercise with details:   
* ```dotworkout
  {
    "name": "push-up",
    "description": "A push-up is a common calisthenics exercise beginning from the prone position.",
    "caloriesBurned": 50,
    "exerciseType": "STRENGTH",
    "equipmentNeeded": "None",
    "difficultyLevel": "BEGINNER",
    "muscleGroups": ["CHEST"]}
    ```
 
* Updating of workout exercise.     
  
* Deletion of workout exercise.

* Viewing registered workout exercise.


### Personalized Workout exercise

* Addition of personalized workout exercise with details:    
* ```dotPersonalizedWorkout
  {
        {
        "workoutExerciseEntity": {
            "id": "434a16b3-2bc1-4946-abcb-9227da4ca58c"  // Ensure that this ID is already registered
        },
        "repetitions": 4,
        "series": 5,
        "weight": 0.0
    }
    ```
   
* Updating of personalized workout exercise.     
  
* Deletion of personalized workout exercise.

* Viewing registered personalized workout exercise.

* Find personalized workout exercise by muscle group.

* Find personalized workout exercise by difficult level.

* Find personalized workout exercise by name

## Docker 

* You can pull the Docker image using this command:
 ```dotdocker
docker push guilhermebauer/fitness_tracking_status:latest
```

* <span style="color:red;"> But remember, for Docker to work, you need to have Docker installed on your machine. Here is the link to the official documentation to install Docker: [Docker Installation Guide](https://docs.docker.com/get-docker/)</span>


## Configuration of the Environment

* To run the project, you need a .env file on your machine with the following configuration:

```dotenv
DB_NAME=local_database
DB_USERNAME=root
DB_PASSWORD=local_password
DB_PORT=3308
SECRET_KEY=local_secret
```

## Swagger

* [Swagger Documentation](http://localhost:8080/swagger-ui/index.html)
  
## Author
 www.linkedin.com/in/guilherme-bauer-desenvolvedor

