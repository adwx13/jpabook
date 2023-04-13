package pri.sungjin.jpabook.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pri.sungjin.jpabook.domain.item.Item;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;


    /**
     * 영속 엔티티 -> JPA가 관리하는 상태의 엔티티로 영속엔티티의 경우 내부 값을 변경시 JPA가 자동으로 DB에 적용해주는 상태
     * 준영속 엔티티 -> 더이상 영속성 컨텍스트가 관리하지 않는 상태의 엔티티
     * merge를 쓰면 준영속 엔티티로 DB에 업데이트치고 영속성 엔티티를 반환해준다
     * merge는 위험하다 만약 merge시킬때 item객체의 내부 객체가 null이 있으면 DB에서 해당 필드의 데이터는 null로 들어가게 된다
     * 따라서 변경감지를 사용하는게 좋다 (findOne해서 가져온 값(영속성)의 데이터를 변경)
     */
    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            Item mergedItem = em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }


}
