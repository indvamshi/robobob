package com.maths.challenge.service;

import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;

/**
 * Interface for handling question requests.
 * Implementations of this interface are responsible for processing a question
 * and generating an answer response.
 */
public interface QuestionHandler {

    /**
     * Handles the given question request and generates an answer response.
     *
     * @param questionRequest The request containing the question to be handled.
     * @return An AnswerResponse object containing the answer.
     */
    AnswerResponse handleQuestion(QuestionRequest questionRequest);
}
