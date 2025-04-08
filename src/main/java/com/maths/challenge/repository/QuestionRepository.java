package com.maths.challenge.repository;

import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;

/**
 *  Repository to retrieve answer either from fileBased or database storage.
 */
public interface QuestionRepository {

    AnswerResponse getAnswer(QuestionRequest questionRequest);

}
