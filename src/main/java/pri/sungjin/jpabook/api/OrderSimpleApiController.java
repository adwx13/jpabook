package pri.sungjin.jpabook.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pri.sungjin.jpabook.domain.Address;
import pri.sungjin.jpabook.domain.Order;
import pri.sungjin.jpabook.domain.OrderStatus;
import pri.sungjin.jpabook.repository.OrderRepository;
import pri.sungjin.jpabook.repository.OrderSearch;
import pri.sungjin.jpabook.repository.OrderSimpleQueryDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * 해당 소스를 실행시키기 위해 JpabookApplication에서 bean으로 Hibernate5JakrataModule을 등록해주었는데
     * 그냥 entity를 반환하지 않는게 좋은 방법이다.
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order: all) {
            //Lazy 강제 초기화
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return all;
    }

    /**
     * Order를 호출 + Order의 member호출 (order갯수만큼) + Order의 delivery호출(order갯수만큼)
     * 만약 Order의 갯수가 2개 일 경우 총 5번 쿼리를 호출한다.
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * fetch 조인으로 데이터를 가져온다.
     * 쿼리 하나로 member, delivery를 가져온다
     * -> 가져오는 필드가 order, member, delivery의 모든 필드를 가져오게 된다 (쓰지 않는 필드까지도)
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
    }

    /**
     * fetch 조인으로 데이터를 가져올 때 해당 dto형식으로만 가져와서 다른 곳에서 재활용할 수 없다.
     * 물론 v3,v4둘다 성능을 비교하면 v4가 빠르다.
     * 재활용성이 높은 v3를 사용하는 것이 좋다.
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderRepository.findOrderDtos();
    }




    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();   //LAZY 초기화
        }
    }


}
