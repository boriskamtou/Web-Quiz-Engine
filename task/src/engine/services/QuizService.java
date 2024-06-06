package engine.services;

import engine.entities.Quiz;
import engine.entities.QuizResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public interface QuizService {
    Quiz createQuiz(Quiz quiz);
    Quiz findQuizById(Long id);
    List<Quiz> findAllQuiz();
    Page<Quiz> findAllQuizPage(Pageable pageable);
    QuizResponse solveQuiz(Long quizId, Map<String, List<Integer>> answer);
    void deleteQuiz(Quiz quiz);
}
