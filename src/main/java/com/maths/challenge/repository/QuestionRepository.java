package com.maths.challenge.repository;

import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;

/**
 * Repository interface for retrieving answers to a question.
 * Implementations of this interface are responsible for fetching answers
 * based on the provided QuestionRequest.
 */
public interface QuestionRepository {

    /**
     * Retrieves the answer for the given question request.
     *
     * @param questionRequest The request containing the question for which to retrieve an answer.
     * @return An AnswerResponse object containing the answer.
     */
    AnswerResponse getAnswer(QuestionRequest questionRequest);

}
