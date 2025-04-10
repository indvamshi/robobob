package com.maths.challenge.service;

import com.maths.challenge.exception.ArithmeticEvaluationException;
import com.maths.challenge.exception.ArithmeticSyntaxException;
import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling arithmetic question requests.
 * It uses GraalVM's JavaScript context to evaluate arithmetic expressions.
 */
@Slf4j
@Service("ArithmeticService")
public class ArithmeticQuestionService implements QuestionHandler {

    /**
     * The language identifier for the JavaScript context used by GraalVM.
     */
    public static final String JS_LANGUAGE = "js";

    /**
     * Handles the given arithmetic question request by evaluating the expression.
     *
     * @param questionRequest The question request containing the arithmetic expression.
     * @return An AnswerResponse object containing the result of the evaluation.
     */
    @Override
    public AnswerResponse handleQuestion(QuestionRequest questionRequest) {
        try {
            Number evaluate = evaluate(questionRequest.getQuestion());
            return new AnswerResponse(evaluate.toString());
        } catch (ArithmeticException exp) {
            log.error("Arithmetic evaluation failed: {}", exp.getMessage());
            throw exp;
        }
    }

    /**
     * Evaluates the given arithmetic expression using GraalVM's JavaScript context.
     *
     * @param expression The arithmetic expression to evaluate.
     * @return The result of the evaluation as a BigDecimal.
     * @throws ArithmeticException If an error occurs during evaluation.
     */
    private Number evaluate(String expression) throws ArithmeticException {
        try (Context context = Context.newBuilder(JS_LANGUAGE)
                .allowAllAccess(false)
                .allowHostAccess(HostAccess.NONE)
                .build()) {

            Value result = context.eval(JS_LANGUAGE, expression);

            if (!result.fitsInDouble()) {
                throw new ArithmeticEvaluationException("Expression did not evaluate to a numeric result.");
            }

            double doubleValue = result.asDouble();

            if (Double.isInfinite(doubleValue) || Double.isNaN(doubleValue)) {
                throw new ArithmeticEvaluationException("Expression evaluated to an invalid number (Infinity or NaN).");
            }

            // checks if value is a whole number
            if (doubleValue == Math.floor(doubleValue)) {
                return (int) doubleValue;
            }

            return doubleValue;
        } catch (PolyglotException exp) {
            log.error("Invalid expression", exp);
            throw new ArithmeticSyntaxException("Invalid expression: " + expression);
        }
    }

}
