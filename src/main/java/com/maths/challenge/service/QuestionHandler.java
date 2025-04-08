package com.maths.challenge.service;

import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;

public interface QuestionHandler {
    AnswerResponse handleQuestion(QuestionRequest questionRequest);
}
