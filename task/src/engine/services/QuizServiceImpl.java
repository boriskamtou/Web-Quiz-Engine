package engine.services;

import engine.adapters.UserAdapter;
import engine.entities.Quiz;
import engine.entities.QuizResponse;
import engine.entities.QuizSolve;
import engine.entities.User;
import engine.exceptions.QuizNotFoundException;
import engine.repositories.QuizRepository;
import engine.repositories.QuizSolveRepository;
import engine.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

@Slf4j
@Service
public class QuizServiceImpl implements QuizService {
    static final Logger LOGGER = Logger.getLogger("QuizServiceImpl");

    private final QuizRepository quizRepository;
    private final QuizSolveRepository quizSolveRepository;
    private final UserRepository userRepository;

    @Autowired
    public QuizServiceImpl(QuizRepository quizRepository, QuizSolveRepository quizSolveRepository, UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.quizSolveRepository = quizSolveRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz findQuizById(Long id) {
        return quizRepository.findById(id).orElse(null);
    }

    @Override
    public List<Quiz> findAllQuiz() {
        return (List<Quiz>) quizRepository.findAll();
    }

    @Override
    public Page<Quiz> findAllQuizPage(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    @Override
    public QuizResponse solveQuiz(Long quizId, Map<String, List<Integer>> answer) {
        Quiz findedQuiz = findQuizById(quizId);
        // Get the current user
        UserAdapter currentUser = (UserAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if (findedQuiz != null) {
            boolean isAnswerCorrect = false;

            List<Integer> quizAnswer = findedQuiz.getAnswer();
            List<Integer> userAnswer = answer.get("answer");

            if (CollectionUtils.isEqualCollection(quizAnswer, userAnswer)) {
                isAnswerCorrect = true;
                User user = userRepository.findByEmail(currentUser.getUsername());

                QuizSolve quizSolve = new QuizSolve();

                quizSolve.setUser(user);
                quizSolve.setQuiz(findedQuiz);
                quizSolve.setCompletedAt(LocalDateTime.now());

                quizSolveRepository.save(quizSolve);
            }

            return isAnswerCorrect ? new QuizResponse(true, "Congratulations, you're right!")
                    : new QuizResponse(false, "Wrong answer! Please, try again.");
        } else {
            throw new QuizNotFoundException();
        }

    }


    @Override
    public void deleteQuiz(Quiz quiz) {
        if (quiz == null) {
            throw new QuizNotFoundException();
        }
        quizRepository.delete(quiz);
    }

}
