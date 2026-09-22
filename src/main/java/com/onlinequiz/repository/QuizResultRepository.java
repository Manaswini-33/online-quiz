package com.onlinequiz.repository;

import com.onlinequiz.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository
        extends JpaRepository<QuizResult, Long> {

    List<QuizResult> findByUserIdOrderByCompletedAtDesc(Long userId);
}