package pri.sungjin.jpabook.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pri.sungjin.jpabook.domain.Member;

import java.util.List;

/**
 * Spring Data Jpa는 interface로 메소드 이름만 통해 기능을 작성할 수 있다.
 */
public interface MemberDataJpaRepository extends JpaRepository<Member, Long> {

    List<Member> findByName(String name);
}
