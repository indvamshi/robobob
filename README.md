# RoboBob Application

The application is built using springboot using contract first approach using OpenAPI. Uses gradle as build tool.

# How to build
use './gradlew clean build' to build the project. It auto generates the classes from OpenAPI spec. 

# How to run
Run Application.java as SpringBoot application

# Requirements
1. Single endpoint /api/questions post method to retrieve answers for basic and arithmetic questions
2. The basic questions are stored under /src/main/resources/questions/basic_questions.txt file. Additional questions
    can be added to the file.
3. The question & answers are retrieved from a file which are loaded into memory during application startup. Its flexible
    to store the questions in permanent db
4. The service QuestionHandlerResolver determines if the request if for basic or arithmetic question.
