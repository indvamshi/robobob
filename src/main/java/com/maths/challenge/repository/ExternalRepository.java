package com.maths.challenge.repository;

import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@Profile("!local")
public class ExternalRepository implements QuestionRepository {
    @Override
    public AnswerResponse getAnswer(QuestionRequest questionRequest) {
        log.info("Retrieving answer to a question : {} from database", questionRequest.getQuestion());
        return new AnswerResponse(null);
    }
}
