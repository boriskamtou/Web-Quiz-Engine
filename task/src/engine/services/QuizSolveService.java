package engine.services;

import engine.entities.QuizSolve;
import engine.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface QuizSolveService {
    Page<QuizSolve> findAll(User user, Pageable pageable);
    void save(QuizSolve quizSolve);
}
