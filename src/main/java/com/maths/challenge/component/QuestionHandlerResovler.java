package com.maths.challenge.component;

import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import com.maths.challenge.service.QuestionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 *  Resolver class decides if the question is of type of basic or arithmetic question
 */
@Component
public class QuestionHandlerResovler {

    private static final Pattern ARITHMETIC_PATTERN = Pattern.compile("^[0-9+\\-*/.\\s()]+$");

    private final QuestionHandler basicQuestionHandler;
    private final QuestionHandler arithmeticQuestionHandler;

    public QuestionHandlerResovler(@Qualifier("BasicQuestionService") QuestionHandler basicQuestionHandler,
                                   @Qualifier("ArithmeticService") QuestionHandler arithmeticQuestionHandler) {
        this.basicQuestionHandler = basicQuestionHandler;
        this.arithmeticQuestionHandler = arithmeticQuestionHandler;
    }

    public AnswerResponse handle(QuestionRequest questionRequest) {
        if (ARITHMETIC_PATTERN.matcher(questionRequest.getQuestion()).matches()) {
            return this.arithmeticQuestionHandler.handleQuestion(questionRequest);
        }
        return this.basicQuestionHandler.handleQuestion(questionRequest);
    }
}
