package com.maths.challenge.resource;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.maths.challenge.component.QuestionHandlerResolver;
import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(QuestionsResource.class)
public class QuestionsResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuestionHandlerResolver questionResolver;

    @Test
    public void testAskQuestionEndpoint_basicQuestion() throws Exception {
        // Given
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestion("What is the capital of England");

        AnswerResponse expectedAnswer = new AnswerResponse();
        expectedAnswer.setAnswer("London");

        when(questionResolver.handle(questionRequest)).thenReturn(expectedAnswer);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/questions") // Assuming your API endpoint is /api/questions
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"question\": \"What is the capital of England\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.answer").value("London"));

        verify(questionResolver).handle(questionRequest);
    }

    @Test
    public void testAskQuestionEndpoint_arithmeticQuestion() throws Exception {
        // Given
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestion("2+2");

        AnswerResponse expectedAnswer = new AnswerResponse();
        expectedAnswer.setAnswer("4");

        when(questionResolver.handle(questionRequest)).thenReturn(expectedAnswer);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/questions") // Assuming your API endpoint is /api/questions
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\": \"2+2\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.answer").value("4"));

        verify(questionResolver).handle(questionRequest);
    }

    @Test
    public void testAskQuestionEndpoint_invalidJson() throws Exception {
        // Given
        String invalidJson = "{\"question\": \"What is the capital of England\""; // Missing closing brace

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()); // Expect a 400 Bad Request status

        verify(questionResolver, never()).handle(org.mockito.ArgumentMatchers.any(QuestionRequest.class));
    }

    @Test
    public void testAskQuestionEndpoint_malformedJson() throws Exception {
        // Given
        String malformedJson = "{question: \"What is the capital of England\"}"; // Missing quotes around key

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(questionResolver, never()).handle(org.mockito.ArgumentMatchers.any(QuestionRequest.class));
    }

    @Test
    public void testAskQuestionEndpoint_nullQuestion() throws Exception {
        // Given
        String nullQuestionJson = "{\"question\": null}";

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nullQuestionJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Validation failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messages").value("must not be null")); // Default message for @Size(min=1)
    }

    @Test
    public void testAskQuestionEndpoint_missingRequiredField() throws Exception {
        // Given
        String emptyQuestionJson = "{\"question\": \"\"}";

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyQuestionJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Validation failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messages").value("size must be between 1 and 2147483647")); // Default message for @Size(min=1)

        verify(questionResolver, never()).handle(org.mockito.ArgumentMatchers.any(QuestionRequest.class));
    }
}
