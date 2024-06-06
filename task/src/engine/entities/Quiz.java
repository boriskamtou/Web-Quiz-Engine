package engine.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String text;

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @Size(min = 2)
    private List<String> options;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> answer;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // Consider LAZY fetching
    @JoinColumn(name = "user_id")
    private User user; // More descriptive name

    @JsonIgnore
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE) // Cascade only for deletion
    private List<QuizSolve> completions;

}