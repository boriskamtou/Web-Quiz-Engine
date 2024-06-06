package engine.repositories;

import engine.entities.QuizSolve;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizSolveRepository extends JpaRepository<QuizSolve, Long> {

    @Query("SELECT c FROM QuizSolve c WHERE c.user.id = ?1 ORDER BY c.completedAt DESC")
    Page<QuizSolve> findAllCompletedQuizzesWithPagination(Long id, Pageable pageable);
}
