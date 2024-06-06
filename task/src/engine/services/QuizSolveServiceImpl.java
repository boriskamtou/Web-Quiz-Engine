package engine.services;

import engine.entities.QuizSolve;
import engine.entities.User;
import engine.repositories.QuizSolveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QuizSolveServiceImpl implements QuizSolveService {
    final QuizSolveRepository quizSolveRepository;

    @Autowired
    public QuizSolveServiceImpl(QuizSolveRepository quizSolveRepository) {
        this.quizSolveRepository = quizSolveRepository;
    }

    @Override
    public Page<QuizSolve> findAll(User user, Pageable pageable) {
        return quizSolveRepository.findAllCompletedQuizzesWithPagination(user.getId(), pageable);
    }

    @Override
    public void save(QuizSolve quizSolve) {
        quizSolveRepository.save(quizSolve);
    }
}
