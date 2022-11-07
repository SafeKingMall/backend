package com.safeking.shop.domain.item.domain.service;

import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Commit
class ItemServiceTest {
    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private EntityManager em;

    @Test
    void save() {
        //기존에 카테고리가 먼저 생성되야함
        Category category1 = new Category("해양사고");
        Category category2 = new Category("중대사고");

        em.persist(category1);
        em.persist(category2);

        em.flush();
        em.clear();

        //ItemSaveDto생성
        List<Long> categories = new ArrayList<>();

        categories.add(category1.getId());

        categories.add(category2.getId());

        ItemSaveDto itemSaveDto = ItemSaveDto.builder()
                .name("안전모")
                .quantity(100)
                .description("설명1")
                .admin(null)
                .categories(categories)
                .price(1000)
                .build();

        //Item 생성
        itemService.save(itemSaveDto);

    }
    @Test
    public void update(){
        //기존에 카테고리가 먼저 생성되야함
        Category category1 = new Category("해양사고");
        Category category2 = new Category("중대사고");
        Category category3 = new Category("누수사고");
        Category category4 = new Category("화재사고");

        em.persist(category1);
        em.persist(category2);
        em.persist(category3);
        em.persist(category4);

        em.flush();
        em.clear();

        //ItemSaveDto생성
        List<Long> categories = new ArrayList<>();

        categories.add(category1.getId());

        categories.add(category2.getId());

        ItemSaveDto itemSaveDto = ItemSaveDto.builder()
                .name("안전모")
                .quantity(100)
                .description("설명2")
                .admin(null)
                .categories(categories)
                .price(1000)
                .build();

        //Item 생성
        Long itemId = itemService.save(itemSaveDto);

        //update시작

        //updateCategories 생성
        List<Long> updateCategories = new ArrayList<>();

        updateCategories.add(category3.getId());

        updateCategories.add(category4.getId());

        //itemUpdateDto생성
        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder()
                .id(itemId)
                .name("안전모")
                .quantity(10)
                .description("설명2")
                .price(1000)
                .categories(updateCategories)
                .build();

        itemService.update(itemUpdateDto);
    }
}