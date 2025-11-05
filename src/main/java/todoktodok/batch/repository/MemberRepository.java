package todoktodok.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.batch.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}