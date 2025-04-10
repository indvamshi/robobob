package com.maths.challenge.exception;

/**
 * Exception indicating that a requested question was not found.
 * This exception is thrown when a question is not found in the data source.
 */
public class QuestionNotFoundException extends RuntimeException {

    public QuestionNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
