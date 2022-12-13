package com.safeking.shop.domain.item.domain.service;

import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.CategoryRepository;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemUpdateDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemViewDto;
import com.safeking.shop.domain.item.web.request.ItemSaveRequest;
import com.safeking.shop.domain.item.web.request.ItemUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ItemService {

    private final CategoryRepository categoryRepository;

    private final ItemRepository itemRepository;

    public Long save(ItemSaveRequest itemSaveRequest){

        Category category = categoryRepository.findById(itemSaveRequest.getCategoryId()).orElseThrow();
        Item item = Item.createItem(itemSaveRequest.getName()
                , itemSaveRequest.getQuantity()
                , itemSaveRequest.getDescription()
                , itemSaveRequest.getPrice()
                , itemSaveRequest.getAdminId()
                , category);

        itemRepository.save(item);

        return item.getId();
    }

    public void update(ItemUpdateRequest itemUpdateRequest){
        //기존에 item이 있다고 가정
        Item item = itemRepository.findById(itemUpdateRequest.getId()).orElseThrow();
        Category category = categoryRepository.findById(itemUpdateRequest.getCategoryId()).orElseThrow();
        item.update(itemUpdateRequest.getName()
                , itemUpdateRequest.getQuantity()
                , itemUpdateRequest.getPrice()
                , itemUpdateRequest.getDescription()
                , itemUpdateRequest.getAdminId()
                , category
        );

    }
    public void delete(Long id){

        Item item = itemRepository.findById(id).orElseThrow();

        itemRepository.delete(item);


    }

    public ItemViewDto view(Long id){
        Item item = itemRepository.findById(id).orElseThrow();
        log.info("Item.viewYn : "+ item.getViewYn());
        ItemViewDto itemViewDto = new ItemViewDto(item.getId(),
                item.getName(),
                item.getQuantity(),
                item.getDescription(),
                item.getPrice(),
                item.getAdminId(),
                item.getCategory().getName(),
                item.getCreateDate().toString(),
                item.getLastModifiedDate().toString(),
                item.getViewYn()
                );

        return itemViewDto;
    }

    public Page<Item> List(Pageable pageable){
        Page<Item> posts = itemRepository.findAll(pageable);
        return posts;
    }


}
