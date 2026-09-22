package com.onlinequiz.controller;

import com.onlinequiz.model.Quiz;
import com.onlinequiz.model.QuizResult;
import com.onlinequiz.model.User;
import com.onlinequiz.repository.QuizResultRepository;
import com.onlinequiz.repository.QuizRepository;
import com.onlinequiz.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class QuizResultController {

    private final QuizResultRepository resultRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    public QuizResultController(
            QuizResultRepository resultRepository,
            UserRepository userRepository,
            QuizRepository quizRepository) {

        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
    }

    // Save quiz result
    @PostMapping
    public ResponseEntity<?> saveResult(
            @RequestBody QuizResultRequest request) {

        try {

            User user = userRepository
                    .findById(request.getUserId())
                    .orElseThrow(() ->
                            new RuntimeException("User not found"));

            Quiz quiz = quizRepository
                    .findById(request.getQuizId())
                    .orElseThrow(() ->
                            new RuntimeException("Quiz not found"));

            QuizResult result = new QuizResult();

            result.setUser(user);
            result.setQuiz(quiz);
            result.setScore(request.getScore());
            result.setTotalQuestions(
                    request.getTotalQuestions()
            );
            result.setCompletedAt(
                    LocalDateTime.now()
            );

            QuizResult savedResult =
                    resultRepository.save(result);

            return ResponseEntity.ok(savedResult);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body("Unable to save quiz result");
        }
    }


    // Get history for one student
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<QuizResult>> getUserHistory(
            @PathVariable Long userId) {

        List<QuizResult> results =
                resultRepository
                        .findByUserIdOrderByCompletedAtDesc(userId);

        return ResponseEntity.ok(results);
    }


    // Request class
    public static class QuizResultRequest {

        private Long userId;
        private Long quizId;
        private int score;
        private int totalQuestions;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getQuizId() {
            return quizId;
        }

        public void setQuizId(Long quizId) {
            this.quizId = quizId;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public void setTotalQuestions(int totalQuestions) {
            this.totalQuestions = totalQuestions;
        }
    }
}