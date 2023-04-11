package pri.sungjin.jpabook;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {
//    @Autowired MemberRepository memberRepository;
//
//
//    //Test케이스에 transactional이 있으면 데이터베이스에 정보를 넣고나서 rollback해버림
//    //Rollback하기 싫으면 @Rollback annotation을 이용하면 됨
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void testMember() throws  Exception {
//        //given
//        Member member = new Member();
//        member.setUsername("memberA");
//
//        //when
//        Long saveId = memberRepository.save(member);
//        Member findMember = memberRepository.find(saveId);
//        //then
//        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
//        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
//        //같은 영속성 컨텍스트 안에서 id가 같으면 같은 entity로 식별
//        Assertions.assertThat(findMember).isEqualTo(member);
//
//
//    }
}