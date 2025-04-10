package com.maths.challenge.service;

import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import com.maths.challenge.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling basic question requests.
 * It delegates the retrieval of answers to a {@link QuestionRepository}.
 */
@Slf4j
@Service("BasicQuestionService")
public class BasicQuestionService implements QuestionHandler {

    /**
     * Repository for retrieving answers to questions.
     */
    private final QuestionRepository questionRepository;

    /**
     * Constructs a new BasicQuestionService with the specified question repository.
     *
     * @param questionRepository The repository for retrieving answers.
     */
    public BasicQuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Handles the given basic question request by retrieving the answer from the repository.
     *
     * @param questionRequest The question request to handle.
     * @return An AnswerResponse object containing the answer retrieved from the repository.
     */
    @Override
    public AnswerResponse handleQuestion(QuestionRequest questionRequest) {
        return questionRepository.getAnswer(questionRequest);
    }
}
