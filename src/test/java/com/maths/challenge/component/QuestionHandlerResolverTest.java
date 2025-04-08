package com.maths.challenge.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import com.maths.challenge.service.QuestionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QuestionHandlerResolverTest {

    @Mock
    private QuestionHandler basicQuestionHandler;

    @Mock
    private QuestionHandler arithmeticQuestionHandler;

    private QuestionHandlerResovler questionHandlerResovler;

    @BeforeEach
    void setUp() {
        questionHandlerResovler = new QuestionHandlerResovler(basicQuestionHandler, arithmeticQuestionHandler);
    }

    @Test
    void testHandleArithmeticQuestion() {
        QuestionRequest request = new QuestionRequest("2 + 2");
        AnswerResponse expectedResponse = new AnswerResponse("4");

        when(arithmeticQuestionHandler.handleQuestion(request)).thenReturn(expectedResponse);

        AnswerResponse actualResponse = questionHandlerResovler.handle(request);

        assertEquals(expectedResponse, actualResponse);
        verify(arithmeticQuestionHandler).handleQuestion(request);
        verify(basicQuestionHandler, never()).handleQuestion(any());
    }

    @Test
    void testHandleBasicQuestion() {
        QuestionRequest request = new QuestionRequest("What is your name");
        AnswerResponse expectedResponse = new AnswerResponse("RoboBob");

        when(basicQuestionHandler.handleQuestion(request)).thenReturn(expectedResponse);

        AnswerResponse actualResponse = questionHandlerResovler.handle(request);

        assertEquals(expectedResponse, actualResponse);
        verify(basicQuestionHandler).handleQuestion(request);
        verify(arithmeticQuestionHandler, never()).handleQuestion(any());
    }

}
