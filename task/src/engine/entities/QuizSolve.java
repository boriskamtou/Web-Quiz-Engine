package engine.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class QuizSolve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER) // Consider LAZY fetching
    @JoinColumn
    private Quiz quiz;

    @JsonIgnore
    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER) // Consider LAZY fetching
    private User user;

    private LocalDateTime completedAt;
}