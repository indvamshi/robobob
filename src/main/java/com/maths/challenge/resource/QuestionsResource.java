package com.maths.challenge.resource;

import com.maths.challenge.component.QuestionHandlerResovler;
import com.maths.challenge.generated.api.QuestionApi;
import com.maths.challenge.generated.model.AnswerResponse;
import com.maths.challenge.generated.model.QuestionRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class QuestionsResource implements QuestionApi {

    private final QuestionHandlerResovler questionResolver;

    public QuestionsResource(QuestionHandlerResovler questionResolver) {
        this.questionResolver = questionResolver;
    }

    @Override
    public ResponseEntity<AnswerResponse> askQuestion(@Valid QuestionRequest questionRequest) {
        return ResponseEntity
                .ok()
                .body(questionResolver.handle(questionRequest));
    }

}
