
package engine.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableSolveQuiz {

    static final Logger LOGGER = Logger.getLogger("QuizServiceImpl");

    private int totalPages;
    private long totalElements;
    private boolean last;
    private boolean first;
    private boolean empty;
    private List<Answer> content;

    static class Answer {
        public Long id;
        public LocalDateTime completedAt;

        public Answer(Long id, LocalDateTime completedAt) {
            this.id = id;
            this.completedAt = completedAt;
        }
    }

    public PageableSolveQuiz convertPageToPageableData(Page<QuizSolve> page, User user) {

        List<QuizSolve> content = page.getContent();

        LOGGER.info("CONTENTS : " + content.size());
        for (var c : content) {
            LOGGER.info("Quiz ID: " + c.getId());
            LOGGER.info("Quiz answer: " + c.getQuiz().getAnswer());
            LOGGER.info(c.getUser().getEmail());
            LOGGER.info("Logged User Email: " + c.getUser().getEmail());
        }


        List<Answer> conList = new ArrayList<>();


        for (var c : content) {
            if (Objects.equals(c.getUser().getId(), user.getId())) {
                var sC = new Answer(c.getQuiz().getId(), c.getCompletedAt());
                conList.add(sC);
            }
        }


        this.content = conList;

        return new PageableSolveQuiz(
                page.getTotalPages(),
                page.getTotalElements(),
                page.isLast(),
                page.isFirst(),
                page.isEmpty(),
                this.content
        );
    }

}

