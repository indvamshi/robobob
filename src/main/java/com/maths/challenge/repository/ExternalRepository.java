package com.maths.challenge.repository;

import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for retrieving answers from an external data source (e.g., a database).
 * This repository is active when the "local" profile is NOT active.
 * It logs the retrieval process and currently returns a placeholder AnswerResponse.
 */
@Slf4j
@Repository
@Profile("!local")
public class ExternalRepository implements QuestionRepository {

    /**
     * Retrieves the answer to a given question from the external data source.
     *
     * @param questionRequest The question request containing the question to answer.
     * @return An AnswerResponse containing the answer (currently a placeholder).
     */
    @Override
    public AnswerResponse getAnswer(QuestionRequest questionRequest) {
        log.info("Retrieving answer to a question : {} from database", questionRequest.getQuestion());
        return new AnswerResponse(null);
    }
}
