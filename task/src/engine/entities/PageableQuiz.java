package engine.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableQuiz {
    private int totalPages;
    private long totalElements;
    private boolean last;
    private boolean first;
    private Sort sort;
    private int number;
    private int numberOfElements;
    private int size;
    private boolean empty;

    private Pageable pageable;
    private List<Quiz> content;

    public PageableQuiz convertPageToPageableData(Page<Quiz> page) {
        return new PageableQuiz(
                page.getTotalPages(),
                page.getTotalElements(),
                page.isLast(),
                page.isFirst(),
                page.getSort(),
                page.getNumber(),
                page.getNumberOfElements(),
                page.getSize(),
                page.isEmpty(),
                page.getPageable(),
                page.getContent()
        );
    }
}

