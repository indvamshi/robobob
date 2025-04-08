package com.maths.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import com.maths.challenge.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BasicQuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private BasicQuestionService basicQuestionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleQuestion_returnsExpectedAnswerResponse() {
        // Given
        QuestionRequest request = new QuestionRequest("What is Java?");
        AnswerResponse expectedResponse = new AnswerResponse("Java is a programming language.");

        when(questionRepository.getAnswer(request)).thenReturn(expectedResponse);

        // When
        AnswerResponse actualResponse = basicQuestionService.handleQuestion(request);

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(questionRepository, times(1)).getAnswer(request);
    }

    @ParameterizedTest
    @CsvSource({
            "What is AI?, AI is Artificial Intelligence.",
            "Explain JVM., JVM stands for Java Virtual Machine.",
            "What is Your Name?, RoboBob."
    })
    void testHandleQuestion_withMultipleInputs(String question, String expectedAnswer) {
        // Given
        QuestionRequest request = new QuestionRequest(question);
        AnswerResponse expectedResponse = new AnswerResponse(expectedAnswer);

        when(questionRepository.getAnswer(request)).thenReturn(expectedResponse);

        // When
        AnswerResponse actualResponse = basicQuestionService.handleQuestion(request);

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(questionRepository).getAnswer(request);
    }

    @Test
    void testHandleQuestion_whenRepositoryThrowsException_shouldPropagateException() {
        // Given
        QuestionRequest request = new QuestionRequest("Trigger error");

        when(questionRepository.getAnswer(request))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                basicQuestionService.handleQuestion(request)
        );

        assertEquals("Database error", thrown.getMessage());
        verify(questionRepository, times(1)).getAnswer(request);
    }
}