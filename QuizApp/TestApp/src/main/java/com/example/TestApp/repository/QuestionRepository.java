package com.example.TestApp.repository;

import com.example.TestApp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT * FROM questions ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Question> getRandomQuestions(@Param("count") int count);

}
