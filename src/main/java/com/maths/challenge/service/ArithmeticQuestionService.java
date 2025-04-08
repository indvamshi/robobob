package com.maths.challenge.service;

import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ArithmeticService")
public class ArithmeticQuestionService implements QuestionHandler {

    @Override
    public AnswerResponse handleQuestion(QuestionRequest questionRequest) {
        return new AnswerResponse(evaluate(questionRequest.getQuestion()).toString());
    }

    /**
     * Evaluates expression using graalvm by creating a context for JS language.
     *
     * @param expression
     * @return a numerical value
     * @throws IllegalArgumentException
     */
    private Number evaluate(String expression) throws IllegalArgumentException {

        try (Context context = Context.newBuilder("js")
                .allowAllAccess(false)             // Disallow all host access
                .allowHostAccess(HostAccess.NONE)  // Don't expose Java types
                .build()) {

            // Evaluate the expression
            Value result = context.eval("js", expression);

            // Ensure it's a number
            if (!result.fitsInDouble()) {
                throw new IllegalArgumentException("Expression did not evaluate to a numeric result.");
            }

            double value = result.asDouble();

            if (Double.isInfinite(value) || Double.isNaN(value)) {
                throw new IllegalArgumentException("Expression evaluated to an invalid number (Infinity or NaN).");
            }

            // checks if value is a whole number
            if (value == Math.floor(value)) {
                return (int) value;
            }
            return value;
        } catch (PolyglotException e) {
            throw new IllegalArgumentException("Invalid expression: " + expression, e);
        }
    }
}
