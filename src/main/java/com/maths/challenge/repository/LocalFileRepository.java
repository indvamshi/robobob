package com.maths.challenge.repository;

import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
@Profile("local")
public class LocalFileRepository implements QuestionRepository {

    private final Map<String, String> questionAnswerMap;
    private final String questionsFilePath;

    public LocalFileRepository(@Value("${robobob.basic-questions-file}") String questionsFilePath) {
        this.questionsFilePath = questionsFilePath;
        this.questionAnswerMap = new ConcurrentHashMap<>();
    }

    /**
     * During application startup init method loads all the questions from basic_questions.txt
     * into memory for fast retrieval.
     * @throws IOException
     */
    @PostConstruct
    public void init() {
        try {
            loadQuestions();
        } catch (IOException exp) {
            log.error("Error reading questions file: {}", exp.getMessage(), exp);
        }
    }

    @Override
    public AnswerResponse getAnswer(QuestionRequest questionRequest) {
        log.info("Retrieving answer to a question : {} from local storage", questionRequest.getQuestion());
        if (!questionAnswerMap.containsKey(questionRequest.getQuestion().trim().toLowerCase()))
            throw new InvalidParameterException("Question does not exist.");

        return new AnswerResponse(questionAnswerMap.get(questionRequest.getQuestion().trim().toLowerCase()));
    }

    private void loadQuestions() throws IOException {
        Path path = Path.of(questionsFilePath);
        if (!Files.exists(path)) {
            log.error("Questions file does not exist: {}", questionsFilePath);
            return;
        }

        Files.lines(path)
                .map(String::trim)
                .filter(line -> line.contains("="))
                .forEach(line -> {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        questionAnswerMap.put(parts[0].trim().toLowerCase(), parts[1].trim());
                    } else {
                        log.warn("Skipping invalid line format: {}", line);
                    }
                });
    }
}
