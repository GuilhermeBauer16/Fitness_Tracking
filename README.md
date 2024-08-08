# Fitness Tracking  

[![Continuous Integration with Github, Github Actions, and Docker Hub](https://github.com/GuilhermeBauer16/E-commerce/actions/workflows/continuous-integration.yml/badge.svg?event=label)](https://github.com/GuilhermeBauer16/E-commerce/actions/workflows/continuous-integration.yml)
## Objective  

the user will log in and use Fitness tracking functions like to personalize the exercise by changing the weight, repetitions, and set. Before the user uses the app, be necessary to sign in or log in.         
    
         
## Working example          

![Screenshot from 2024-08-08 19-16-00](https://github.com/user-attachments/assets/f44b39c3-f804-45ea-84be-36832629d856)


## Sending 

![Screenshot from 2024-08-08 19-18-27](https://github.com/user-attachments/assets/4c635e45-a7bd-450e-b6a4-77ba2e96b3a5)


## Response 

![Screenshot from 2024-08-08 19-19-11](https://github.com/user-attachments/assets/ec419e37-ee13-4782-8368-05f56a50b9e9)


## Learnings 

In that project, I learned how to create a login system using Spring Security and JWT(JSON Web Token ) tokens to structure a login system. I also learned and implemented the unit tests using Mockito and JUnit. To create the integration tests, I went up a MySQL container so the tests are near a production application. I learned how to implement a docker-compose. I implemented a CI/CD in the GitHub main of my project. After the update, that update will reflected in the Docker Hub repository, generating a new latest version.




## Functionalities

## User 
* Login of the user with Spring security authentication.
* ```dotlogin
    {
    "email": "john@hotmail.com",(ensure of that email is already registraded)
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
* Addition of workout exercise divided between:    
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

* Addition of personalized workoutexercise divided between:    
* ```dotPersonalizedWorkout
  {
        {
        "workoutExerciseEntity": {
            "id": "434a16b3-2bc1-4946-abcb-9227da4ca58c"(ensure of that ID is already registrated)
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


## Configuration of the environment

* but remember for the project work is need to have a archive called `.env` in your machine.

```dotenv
DB_NAME=local_database
DB_USERNAME=root
DB_PASSWORD=local_password
DB_PORT=3308
SECRET_KEY=local_secret
EXPIRE_LENGTH=3600000
```

## Swagger

* [Swagger Documentation](http://localhost:8080/swagger-ui/index.html)
  
## Author
 www.linkedin.com/in/guilherme-bauer-desenvolvedor

