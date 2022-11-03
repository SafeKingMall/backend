package com.safeking.shop.domain.item.domain.service;

import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionUpdateDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
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
class ItemQuestionServiceTest {

    @Autowired
    private ItemQuestionService itemQuestionService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private EntityManager em;

    @Test
    void CUD() {

        //아이템을 먼저 생성
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
        Long itemId = itemService.save(itemSaveDto);

        //ItemQuestionSaveDto생성
        ItemQuestionSaveDto itemQuestionSaveDto = ItemQuestionSaveDto.builder()
                .itemId(itemId)
                .contents("설명1")
                .title("제목1")
                .member(null)
                .build();

        Long itemQuestionId = itemQuestionService.save(itemQuestionSaveDto);

        //ItemQuestionUpdateDto생성
        ItemQuestionUpdateDto itemQuestionUpdateDto = ItemQuestionUpdateDto.builder()
                .id(itemQuestionId)
                .contents("설명1에서 2로 변경")
                .title("제목1에서 2로 변경")
                .build();

        itemQuestionService.update(itemQuestionUpdateDto);

//        itemQuestionService.delete(itemQuestionId);

    }
}