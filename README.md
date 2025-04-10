# RoboBob Application

The application is built and tested using 
1. Java 17
2. Gradle build tool
3. Springboot
4. OpenAPI
5. Junit5
6. Mockito

# How to build
use './gradlew clean build' to build the project. It auto generates the classes from OpenAPI spec. 

# How to run
Run Application.java as SpringBoot application

# Documentation
1. Single endpoint /api/questions post method to retrieve answers for basic and arithmetic questions
2. The basic questions are stored under /src/main/resources/questions/basic_questions.txt file. Additional questions
    can be added to the file.
3. The question & answers are retrieved from a file which are loaded into memory during application startup. It is flexible
    to store the questions in db.
4. The service QuestionHandlerResolver determines if the request is for basic or arithmetic question.
5. Used profile as local which is set as active
6. GraalVM used for evaluating arithmetic expressions.
7. Added custom exception handlers

# Additional Considerations
1. Pact tests can be written.
