package todoktodok.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.batch.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}