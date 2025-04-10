package com.maths.challenge.resource;

import com.maths.challenge.component.QuestionHandlerResolver;
import com.maths.challenge.generated.api.QuestionApi;
import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling question requests.
 * This class implements the {@link QuestionApi} interface and exposes an endpoint
 * to receive and process questions, delegating the handling to a {@link QuestionHandlerResolver}.
 */
@Slf4j
@RestController
@RequestMapping("/")
public class QuestionsResource implements QuestionApi {

    private final QuestionHandlerResolver questionResolver;

    public QuestionsResource(QuestionHandlerResolver questionResolver) {
        this.questionResolver = questionResolver;
    }

    /**
     * Handles the incoming question request and returns the answer response.
     *
     * @param questionRequest The question request to handle.
     * @return A ResponseEntity containing the answer response.
     */
    @Override
    public ResponseEntity<AnswerResponse> askQuestion(@Valid QuestionRequest questionRequest) {
        log.info("Received question: {}", questionRequest.getQuestion());
        AnswerResponse answer = questionResolver.handle(questionRequest);
        log.info("Answer: {}", answer.getAnswer());
        return ResponseEntity.ok(answer);
    }

}
