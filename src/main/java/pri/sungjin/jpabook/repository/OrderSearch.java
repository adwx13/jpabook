package pri.sungjin.jpabook.repository;

import lombok.Getter;
import lombok.Setter;
import pri.sungjin.jpabook.domain.OrderStatus;

@Getter @Setter
public class OrderSearch {

    private String memberName;  //회원 이름
    private OrderStatus orderStatus;
}
