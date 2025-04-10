package com.maths.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.maths.challenge.exception.ArithmeticEvaluationException;
import com.maths.challenge.exception.ArithmeticSyntaxException;
import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class ArithmeticQuestionServiceTest {

    private ArithmeticQuestionService service;

    @BeforeEach
    public void setup() {
        service = new ArithmeticQuestionService();
    }

    @Nested
    @DisplayName("Valid Expression Tests")
    class ValidExpressionTests {

        static Stream<TestData> validExpressions() {
            return Stream.of(
                    new TestData("2 + 3 * 4", "14"),
                    new TestData("(2 + 3) * 4", "20"),
                    new TestData("5.5 + 2.5", "8"),
                    new TestData("5.5 + 2.6", "8.1"),
                    new TestData("7 / 2", "3.5"),
                    new TestData("6 / 3", "2"),
                    new TestData("1 + 2 + 3 + 4 + 5", "15"),
                    new TestData("2+2", "4"),
                    new TestData("2*10.5+1", "22")
            );
        }

        @ParameterizedTest(name = "Expression: \"{0}\" should return {1}")
        @MethodSource("validExpressions")
        void testHandleQuestion_testValidArithmetic(TestData data) {
            QuestionRequest request = new QuestionRequest(data.expression());
            AnswerResponse response = service.handleQuestion(request);
            assertNotNull(response);
            assertEquals(data.expectedResult(), response.getAnswer());
        }
    }

    @Nested
    @DisplayName("Evaluation Syntax Exception Tests")
    class EvaluationSyntaxExpressionTests {

        static Stream<String> syntaxExpressions() {
            return Stream.of(
                    "2 + * 5",
                    "(3 + 2",
                    "2 + (3 * )",
                    "2 + three",
                    "abc123"
            );
        }

        @ParameterizedTest(name = "Expression: \"{0}\" should throw ArithmeticSyntaxException")
        @MethodSource("syntaxExpressions")
        void testHandleQuestion_withMalformedExpressions(String expression) {
            assertThrows(ArithmeticSyntaxException.class, () ->
                    service.handleQuestion(new QuestionRequest(expression)));
        }
    }

    @Nested
    @DisplayName("Evaluation Bad Expression Tests")
    class EvaluationBadExpressionTests {

        static Stream<String> badExpressions() {
            return Stream.of(
                    "5 / 0",
                    " 0 / 0",
                    "",
                    "    "
            );
        }

        @ParameterizedTest(name = "Expression: \"{0}\" should throw ArithmeticEvaluationException")
        @MethodSource("badExpressions")
        void testHandleQuestion_withMalformedExpressions(String expression) {
            assertThrows(ArithmeticEvaluationException.class, () ->
                    service.handleQuestion(new QuestionRequest(expression)));
        }
    }

    record TestData(String expression, String expectedResult) {
    }
}

