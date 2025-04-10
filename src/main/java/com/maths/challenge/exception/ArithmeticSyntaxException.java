package com.maths.challenge.exception;

/**
 * Custom exception for syntax errors in arithmetic expressions.
 */
public class ArithmeticSyntaxException extends ArithmeticException {

    public ArithmeticSyntaxException(String errorMessage) {
        super(errorMessage);
    }
}
