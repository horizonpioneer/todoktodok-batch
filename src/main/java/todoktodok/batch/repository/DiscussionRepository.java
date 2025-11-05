package todoktodok.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.batch.entity.Discussion;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
}