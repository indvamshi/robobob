package com.maths.challenge.exception;

/**
 * Custom exception for evaluation errors in arithmetic expressions.
 */
public class ArithmeticEvaluationException extends ArithmeticException {

    public ArithmeticEvaluationException(String errorMessage) {
        super(errorMessage);
    }
}
