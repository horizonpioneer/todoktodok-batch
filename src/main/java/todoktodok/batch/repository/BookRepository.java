package todoktodok.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.batch.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}