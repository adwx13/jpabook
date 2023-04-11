package pri.sungjin.jpabook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pri.sungjin.jpabook.domain.Member;
import pri.sungjin.jpabook.repository.MemberRepository;

import java.util.List;


//Data변경을 위해 Transactional안에서 수행해야한다.
//class에 해당 어노테이션을 넣으면 내부메소드들에 모두 적용된다.
//RequiredArgsConstructor를 넣으면 final이 붙은 객체를 받도록 생성자를 만들어준다 (lombok)
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //생성자가 하나인 경우 Autowired가 없어도 연결해줌
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }



    //회원 가입
    //회원가입시에는 readOnly트랜잭션이 아니기 때문에 false(default)로 설정해줘야함
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);    //중복 회원 검증
        memberRepository.save(member);
        //memberRepository에서 save호출시 영속성 컨텍스트에 멤버객체가 들어가는데 그때
        //id값을 채워주기때문에 id가 생겨있다
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    //Transactional에 readOnly를 옵션으로 주면 최적화가 된다
//    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }


}
