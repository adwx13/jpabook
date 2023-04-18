package pri.sungjin.jpabook.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pri.sungjin.jpabook.domain.Order;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        return em.createQuery("select o from Order o join o.member m" +
                        " where o.status = :status " +
                        " and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000)    //최대 1000건
                .getResultList();
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
//주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
//회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class) .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o"+
                " join fetch o.member m"+
                " join fetch o.delivery d", Order.class).getResultList();

    }

    /**
     * 해당 메소드는 DTO를 반환하는 메소드로
     * 별도 repository에 만드는 것이 좋다
     * -> repository.order.simplequery.OrderSimpleQueryRepository repository를 만들어서 거기에다가 관리
     * OrderRepository는 entity만 관리하는 로직이 남아있도록 하기위해
     */
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "select new pri.sungjin.jpabook.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o"+
                        " join o.member m"+
                        " join o.delivery d", OrderSimpleQueryDto.class).getResultList();
    }

    /**
     * distinct를 넣으면 중복된 데이터를 제거해주는데 현재 넣지않아도 중복제거를 해주는 상황
     * -> 현재(컬렉션 페치조인)의 경우 페이징처리가 안된다. 쿼리에 limit, offset을 넣지않고 메모리로 다불러와서 갯수만큼 던져줌
     */
    public List<Order> findAllWithItem() {
        return em.createQuery("select distinct o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d" +
                " join fetch o.orderItems oi" +
                " join fetch oi.item i", Order.class)
                .setFirstResult(1)
                .setMaxResults(100)
                .getResultList();

    }

    /**
     * xToOne관계의 필드는 페치조인으로 가져오며 그외에 컬렉션은 batch_fetch_size 설정을 통해서 해결할 수 있다.(글로벌설정)
     * 개별설정의 경우 @BatchSize 어노테이션 사용
     */
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o"+
                        " join fetch o.member m"+
                        " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
