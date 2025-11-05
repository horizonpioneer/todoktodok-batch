package todoktodok.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.batch.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}