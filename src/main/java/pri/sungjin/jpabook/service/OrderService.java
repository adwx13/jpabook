package pri.sungjin.jpabook.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pri.sungjin.jpabook.domain.Delivery;
import pri.sungjin.jpabook.domain.Member;
import pri.sungjin.jpabook.domain.Order;
import pri.sungjin.jpabook.domain.OrderItem;
import pri.sungjin.jpabook.domain.item.Item;
import pri.sungjin.jpabook.repository.ItemRepository;
import pri.sungjin.jpabook.repository.MemberRepository;
import pri.sungjin.jpabook.repository.OrderRepository;

import java.util.List;

//엔티티에 비즈니스 로직이 대부분 있으면 도메인 모델 패턴
//서비스에 비즈니스 로직이 대부분 있다면 트랜잭션 스크립트 패턴
//현재는 엔티티에 비즈니스 로직들 있으므로 도메인 모델 패턴이다.

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        //Order를 persist하면
        //OneToMany, OneToOne으로 묶인 Delivery나 OrderItem은 Cascade옵션으로 인해 자동 persist된다
        orderRepository.save(order);

        return order.getId();
    }


    //취소
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }


    //검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }


}
