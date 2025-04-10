package com.maths.challenge.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.maths.challenge.exception.QuestionNotFoundException;
import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LocalFileRepositoryTest {

    private final Path validTempFilePath = Paths.get(System.getProperty("java.io.tmpdir"), "test-questions-valid.txt");
    private final String invalidFilePath = "/invalid/path/nonexistent.txt";

    private LocalFileRepository validRepo;
    private LocalFileRepository invalidRepo;

    @BeforeAll
    void setUp() throws IOException {
        // Set up valid repo with a real file
        List<String> lines = List.of(
                "What is Java?=Java is a programming language.",
                "  Trim this   =   Trimmed answer"
        );
        Files.write(validTempFilePath, lines);
        validRepo = new LocalFileRepository(validTempFilePath.toString());
        validRepo.init();

        // Set up invalid repo with non-existent file
        invalidRepo = new LocalFileRepository(invalidFilePath);
        invalidRepo.init(); // Should handle missing file gracefully
    }

    @AfterAll
    void cleanUp() throws IOException {
        Files.deleteIfExists(validTempFilePath);
    }

    @ParameterizedTest
    @ValueSource(strings = {"What is Java?", "what is java?"})
    void testGetAnswer_returnsCorrectAnswerForValidQuestion(String question) {
        QuestionRequest request = new QuestionRequest(question);
        AnswerResponse response = validRepo.getAnswer(request);
        assertNotNull(response);
        assertEquals("Java is a programming language.", response.getAnswer());
    }

    @Test
    void testGetAnswer_returnsCorrectAnswerForValidQuestionWithExtraSpaces() {
        QuestionRequest request = new QuestionRequest("  Trim this  ");
        AnswerResponse response = validRepo.getAnswer(request);
        assertNotNull(response);
        assertEquals("Trimmed answer", response.getAnswer());
    }

    @Test
    void testGetAnswer_returnsNullForUnknownQuestion() {
        assertThrows(QuestionNotFoundException.class, () ->
                validRepo.getAnswer(new QuestionRequest("Unknown question")));
    }

    @Test
    void testGetAnswer_shouldNotThrowExceptionWhenFileIsMissing() {
        assertThrows(QuestionNotFoundException.class, () ->
                invalidRepo.getAnswer(new QuestionRequest("Unknown question")));
    }

}
