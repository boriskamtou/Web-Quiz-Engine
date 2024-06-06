package engine.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotBlank
    @Email(regexp = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$")
    private String email;

    @NotBlank
    @Size(min = 5)
    private String password; // Consider secure storage for password

//    @JsonIgnore
//    @OneToMany(mappedBy = "user") // User can have many completions
//    private List<QuizSolve> completions;
//
//    // Optional: If users can create quizzes
//    @JsonIgnore
//    @OneToMany(mappedBy = "user") // User can create many quizzes
//    private List<Quiz> quizzes;
}
