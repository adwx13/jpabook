package pri.sungjin.jpabook.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    //Order테이블의 member를 연결해서 데이터를 가져오도록 설정
    //해당 컬렉션의 내용은 변경하지 않도록 한다 -> 영속화할 때 hibernate가 사용하는 컬렉션형태로 변경하기때문에 소스로 변경을 일으키면
    //하이버네이트가 정상동작하지 않는다.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
