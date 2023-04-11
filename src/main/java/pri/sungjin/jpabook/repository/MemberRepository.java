package pri.sungjin.jpabook.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pri.sungjin.jpabook.domain.Member;

import java.util.List;

//RequiredArgsConstructor로 인해 EntityManager객체를 생성자로 주입받아온다.
//lombok으로 인해 생성자 자동 생성
@Repository
@RequiredArgsConstructor
public class MemberRepository {


    //Spring이 EntityManager를 만들어서 넣어줌
//    @PersistenceContext
    private final EntityManager em;


    public void save(Member member) {
        //persist한다고 해서 바로 insert문이 나가질 않는다
        //db에 커밋될 때(transactional이 커밋되는시점)에 insert문이 들어감
        //transactional어노테이션의 옵션인 rollback이 false인 경우 insert문이 날라가지 않음
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        //JPQL 사용한 것
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name",name)
                .getResultList();
    }



}
