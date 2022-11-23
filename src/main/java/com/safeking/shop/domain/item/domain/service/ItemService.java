package com.safeking.shop.domain.item.domain.service;

import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.entity.CategoryItem;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.CategoryItemRepository;
import com.safeking.shop.domain.item.domain.repository.CategoryRepository;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.item.domain.service.servicedto.category.CategoryDto;
import com.safeking.shop.domain.item.domain.service.servicedto.category.CategorySaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemUpdateDto;
import com.safeking.shop.global.exception.EmailDuplicateException;
import com.safeking.shop.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final CategoryRepository categoryRepository;

    private final ItemRepository itemRepository;

    private final CategoryItemRepository categoryItemRepository;


    public Long save(ItemSaveDto itemSaveDto){

        Item item = Item.createItem(itemSaveDto.getName(), itemSaveDto.getQuantity(), itemSaveDto.getDescription(), itemSaveDto.getPrice(), itemSaveDto.getAdminId());

        itemRepository.save(item);

        createCategoryItem(itemSaveDto.getCategories(), item);


        return item.getId();
    }

    public void update(ItemUpdateDto itemUpdateDto){
        //기존에 item이 있다고 가정
        Item item = itemRepository.findById(itemUpdateDto.getId()).orElseThrow();

        item.update(itemUpdateDto.getName(), itemUpdateDto.getQuantity(), itemUpdateDto.getPrice(), itemUpdateDto.getDescription(), itemUpdateDto.getAdminId());

        List<Long> categories = itemUpdateDto.getCategories();

        //삭제 후 다시 생성
        categoryItemRepository.deleteByItem(item);

        createCategoryItem(itemUpdateDto.getCategories(),item);

    }
    public void delete(Long id){

        Item item = itemRepository.findById(id).orElseThrow();



        //item과 연관된 CategoryItem도 삭제
        categoryItemRepository.deleteByItem(item);

        itemRepository.delete(item);


    }

    private void createCategoryItem(List<Long> categories, Item item) {

        for (Long categoryId : categories) {

            Category category = categoryRepository.findById(categoryId).orElseThrow();

            CategoryItem categoryItem = new CategoryItem(category, item);

            categoryItemRepository.save(categoryItem);
        }
    }

    public Item findById(Long id){
        return itemRepository.findById(id).orElseThrow();
    }


}
