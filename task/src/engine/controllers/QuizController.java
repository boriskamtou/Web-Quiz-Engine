package engine.controllers;

import engine.entities.*;
import engine.exceptions.QuizNotFoundException;
import engine.exceptions.UnAuthorizeActionException;
import engine.services.QuizService;
import engine.services.QuizSolveService;
import engine.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class QuizController {

    static final Logger LOGGER = Logger.getLogger("QuizController");

    static final int PAGE_NUMBER = 10;
    private final QuizService quizService;
    private final UserService userService;
    private final QuizSolveService quizSolveService;

    @Autowired
    public QuizController(QuizService quizService, UserService userService, QuizSolveService quizSolveService) {
        this.quizService = quizService;
        this.userService = userService;
        this.quizSolveService = quizSolveService;
    }

    @PostMapping("/quizzes")
    public ResponseEntity<?> createQuiz(
            @RequestBody @Valid Quiz quiz,
            @AuthenticationPrincipal User user
    ) {
        quiz.setUser(user);
        var createdQuiz = quizService.createQuiz(quiz);
        return ResponseEntity.ok(createdQuiz);
    }

    @GetMapping("/quizzes")
    public ResponseEntity<?> findAllQuiz() {
        try {
            List<Quiz> quizList = quizService.findAllQuiz();
            return ResponseEntity.ok(quizList);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(value = "/quizzes", params = "page")
    public ResponseEntity<?> findAllQuizPage(
            @RequestParam("page") int page
    ) {
        try {
            Page<Quiz> quizPage = quizService.findAllQuizPage(PageRequest.of(page, PAGE_NUMBER));
            PageableQuiz pageableQuiz = new PageableQuiz().convertPageToPageableData(quizPage);
            return ResponseEntity.ok(pageableQuiz);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/quizzes/{id}")
    public ResponseEntity<?> findQuizById(@PathVariable Long id) {
        Quiz findedQuiz = quizService.findQuizById(id);
        if (findedQuiz != null) {
            return ResponseEntity.ok(findedQuiz);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/quizzes/{id}/solve")
    public ResponseEntity<?> postUserAnswer(
            @PathVariable(name = "id") Long quizId,
            @RequestBody Map<String, List<Integer>> answer
    ) {
        try {
            QuizResponse quizResponse = quizService.solveQuiz(quizId, answer);
            if (quizResponse != null) {
                return ResponseEntity.ok(quizResponse);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/quizzes/completed")
    public ResponseEntity<?> getAllCompletedQuiz(@RequestParam("page") int page, @AuthenticationPrincipal UserDetails user) {
        User u = userService.findByEmail(user.getUsername());
        Page<QuizSolve> quizSolve = quizSolveService.findAll(u, PageRequest.of(page, PAGE_NUMBER, Sort.by("completedAt").descending()));
        PageableSolveQuiz pageableSolveQuiz = new PageableSolveQuiz().convertPageToPageableData(quizSolve, u);
        return ResponseEntity.ok(pageableSolveQuiz);
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable(name = "id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Quiz quiz = quizService.findQuizById(id);

        LOGGER.info("FOUNDED QUIZ " + quiz);

        if (quiz == null) {
            LOGGER.info("QUIZ NOT FOUND ");
            return ResponseEntity.notFound().build();
        }

        if (user == null) {
            LOGGER.info("USER NOT CONNECTED - FORBIDDEN");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (quiz.getUser() != null) {
            LOGGER.info("USER QUIZ NOT NULL");

            if (quiz.getUser().equals(user)) {
                LOGGER.info("USER QUIZ NOT NULL AND EQUAL TO USER");
                quizService.deleteQuiz(quiz);
                return ResponseEntity.noContent().build();
            } else {
                LOGGER.info("USER QUIZ NOT NULL AND NOT EQUAL TO USER ");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            boolean canDelete = false;
            for (var solve : quiz.getCompletions()) {
                if (solve.getUser() != null) {
                    if (solve.getUser().equals(user)) {
                        LOGGER.info("CAN DELETED");
                        LOGGER.info("USER: " + solve.getUser().getEmail());
                        LOGGER.info("QUIZ DELETED");
                        quizService.deleteQuiz(quiz);
                        return ResponseEntity.noContent().build();
                    } else {
                        LOGGER.info("SOLVE USER QUIZ DIFFERENT");
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    }
                } else {
                    LOGGER.info("QUIZ DELETED");
                    quizService.deleteQuiz(quiz);
                    return ResponseEntity.noContent().build();
                }
            }

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
