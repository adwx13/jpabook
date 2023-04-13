package pri.sungjin.jpabook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pri.sungjin.jpabook.domain.item.Book;
import pri.sungjin.jpabook.domain.item.Item;
import pri.sungjin.jpabook.repository.ItemRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Long itemId, Book bookParam) {
        //findOne으로 불러온 경우 영속성 엔티티이기 때문에 내부 객체를 변경시켜버리면 알아서 DB업데이트가 이루어진다
        //영속성 엔티티를 변경할때 setter사용보다는 changeData등 엔티티내부에 메소드를 만들어서 변경시켜주는게 좋다.
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(bookParam.getPrice());
        findItem.setName(bookParam.getName());
        findItem.setStockQuantity(bookParam.getStockQuantity());
        return findItem;
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }




}
