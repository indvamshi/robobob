package com.maths.challenge.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.script.ScriptException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST API endpoints.
 * This class handles various exceptions and provides consistent error responses.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles validation errors when request parameters are invalid.
     *
     * @param ex      The MethodArgumentNotValidException.
     * @param headers The HttpHeaders.
     * @param status  The HttpStatusCode.
     * @param request The WebRequest.
     * @return ResponseEntity containing the error response.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors
        );
        log.warn("Validation failure: {}", errors);
        return buildResponseEntity(errorResponse);
    }

    /**
     * Handles BadRequestException, indicating an invalid request.
     *
     * @param ex The BadRequestException.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Question is invalid",
                Collections.singletonList(ex.getMessage())
        );
        log.warn("Bad request: {}", ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    /**
     * Handles QuestionNotFoundException, indicating that the requested question was not found.
     *
     * @param ex The QuestionNotFoundException.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(QuestionNotFoundException.class)
    protected ResponseEntity<Object> handleInvalidParameterException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                "Not found",
                Collections.singletonList(ex.getMessage())
        );
        log.warn("Question not found: {}", ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    /**
     * Handles ArithmeticException and ScriptException, indicating errors in arithmetic calculations or scripting.
     *
     * @param ex The ArithmeticException or ScriptException.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler({ArithmeticEvaluationException.class, ArithmeticSyntaxException.class})
    protected ResponseEntity<Object> handleArithmeticErrors(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Invalid arithmetic expression",
                Collections.singletonList(ex.getMessage())
        );
        log.warn("Arithmetic error: {}", ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    /**
     * Handles all other exceptions, providing a generic error response.
     *
     * @param ex      The Exception.
     * @param request The WebRequest.
     * @return ResponseEntity containing the generic error response.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                Collections.singletonList("Please check your question")
        );

        return buildResponseEntity(errorResponse);
    }

    /**
     * Builds a ResponseEntity from an ErrorResponse object.
     *
     * @param errorResponse The ErrorResponse object.
     * @return ResponseEntity containing the error response.
     */
    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    /**
     * Represents the error response structure.
     */
    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private final LocalDateTime timestamp = LocalDateTime.now();
        private final int status;
        private final String error;
        private final List<String> messages;

        /**
         * Constructor for ErrorResponse with HttpStatus.
         *
         * @param status   The HttpStatus.
         * @param error    The error message.
         * @param messages The list of error messages.
         */
        public ErrorResponse(HttpStatus status, String error, List<String> messages) {
            this.status = status.value();
            this.error = error;
            this.messages = messages;
        }
    }
}
