package com.maths.challenge.service;

import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import com.maths.challenge.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("BasicQuestionService")
public class BasicQuestionService implements QuestionHandler {

    private final QuestionRepository questionRepository;

    public BasicQuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public AnswerResponse handleQuestion(QuestionRequest questionRequest) {
        return questionRepository.getAnswer(questionRequest);
    }
}
