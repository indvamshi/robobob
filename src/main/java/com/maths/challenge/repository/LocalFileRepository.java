package com.maths.challenge.repository;

import com.maths.challenge.exception.QuestionNotFoundException;
import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Repository implementation for retrieving answers from a local file.
 * This repository is active when the "local" profile is active.
 * It loads question-answer pairs from a file into memory during application startup.
 */
@Slf4j
@Repository
@Profile("local")
public class LocalFileRepository implements QuestionRepository {

    /**
     * Delimiter used to separate questions and answers in the file.
     */
    private static final String QUESTION_ANSWER_DELIMITER = "=";
    /**
     * In-memory map to store question-answer pairs.
     */
    private final Map<String, String> questionAnswerMap;
    /**
     * Path to the file containing question-answer pairs.
     */
    private final String questionsFilePath;

    /**
     * Constructs a new LocalFileRepository with the specified file path.
     *
     * @param questionsFilePath The path to the file containing question-answer pairs.
     */
    public LocalFileRepository(@Value("${robobob.basic-questions-file}") String questionsFilePath) {
        this.questionsFilePath = questionsFilePath;
        this.questionAnswerMap = new ConcurrentHashMap<>();
    }

    /**
     * Loads question-answer pairs from the file into memory during application startup.
     *
     * @throws IOException If an error occurs while reading the file.
     */
    @PostConstruct
    public void init() {
        try {
            loadQuestions();
        } catch (IOException exp) {
            log.error("Error reading questions file: {}", exp.getMessage(), exp);
        }
    }

    /**
     * Retrieves the answer for the given question request from the in-memory map.
     *
     * @param questionRequest The request containing the question to answer.
     * @return An AnswerResponse object containing the answer.
     * @throws QuestionNotFoundException If the question is not found in the map.
     */
    @Override
    public AnswerResponse getAnswer(QuestionRequest questionRequest) {
        String question = questionRequest.getQuestion().trim().toLowerCase();
        log.info("Retrieving answer to a question : {} from local storage", question);
        if (!questionAnswerMap.containsKey(question)) {
            log.warn("Question not found: {}", question);
            throw new QuestionNotFoundException("Question does not exist.");
        }

        return new AnswerResponse(questionAnswerMap.get(question));
    }

    /**
     * Loads question-answer pairs from the file into the in-memory map.
     *
     * @throws IOException If an error occurs while reading the file.
     */
    private void loadQuestions() throws IOException {
        Path path = Path.of(questionsFilePath);
        if (!Files.exists(path)) {
            log.error("Questions file does not exist: {}", questionsFilePath);
            return;
        }

        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            long count = lines.map(String::trim)
                    .filter(line -> line.contains(QUESTION_ANSWER_DELIMITER))
                    .peek(line -> {
                        String[] parts = line.split(QUESTION_ANSWER_DELIMITER, 2);
                        if (parts.length == 2) {
                            questionAnswerMap.put(parts[0].trim().toLowerCase(), parts[1].trim());
                        } else {
                            log.warn("Skipping invalid line format: {}", line);
                        }
                    }).count();
            log.info("Loaded {} question-answer pairs from file: {}", count, questionsFilePath);
        }
    }
}
