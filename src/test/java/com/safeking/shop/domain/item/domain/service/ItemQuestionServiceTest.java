package com.safeking.shop.domain.item.domain.service;

import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import com.safeking.shop.domain.item.domain.repository.ItemQuestionRepository;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionUpdateDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemQuestionServiceTest {

    @Autowired ItemQuestionService itemQuestionService;
    @Autowired ItemQuestionRepository itemQuestionRepository;
    @Autowired ItemService itemService;
    @Autowired EntityManager em;

    @Test
    void CUD() {

        //아이템을 먼저 생성
        Long itemId = createItem();

        //ItemQuestionSaveDto 생성
        ItemQuestionSaveDto itemQuestionSaveDto = createItemQuestionSaveDto(itemId);

        //ItemQuestion 생성
        Long itemQuestionId = itemQuestionService.save(itemQuestionSaveDto);

        //SAVE 검증로직
        ItemQuestion itemQuestion = itemQuestionRepository.findById(itemQuestionId).orElseThrow();

        assertEquals("itemQuestionSaveDto  와 itemQuestion 의 title 은 같아야한다.",
                itemQuestionSaveDto.getTitle(),itemQuestion.getTitle());
        assertEquals("itemQuestionSaveDto 와 itemQuestion 의 contents 는  같아야한다.",
                itemQuestionSaveDto.getContents(),itemQuestion.getContents());
        assertEquals("itemQuestionSaveDto 와 itemQuestion 의 itemId는 같아야한다.",
                itemQuestionSaveDto.getItemId(),itemQuestion.getItem().getId());

        //ItemQuestionUpdateDto 생성
        ItemQuestionUpdateDto itemQuestionUpdateDto = createItemQuestionUpdateDto(itemQuestionId);

        //UPDATE 실행
        itemQuestionService.update(itemQuestionUpdateDto);

        //UPDATE 검증로직
        assertEquals("itemQuestionUpdateDto 의 title 와 itemQuestion 의 title 은 같아야한다.",
                itemQuestionUpdateDto.getTitle(),itemQuestion.getTitle());
        assertEquals("itemQuestionUpdateDto 의 contents 와  itemQuestion 의 contents 은 같아야한다.",
                itemQuestionUpdateDto.getContents(),itemQuestion.getContents());

        //DELETE 실행
        itemQuestionService.delete(itemQuestionId);

        //DELETE 검증
        //삭제한 itemQuestion 의 id로 조회시에 NoSuchElementException 이 발생해아한다.
        assertThrows(NoSuchElementException.class,
                ()->itemQuestionRepository.findById(itemQuestionId).get());
    }

    private static ItemQuestionUpdateDto createItemQuestionUpdateDto(Long itemQuestionId) {
        ItemQuestionUpdateDto itemQuestionUpdateDto = ItemQuestionUpdateDto.builder()
                .id(itemQuestionId)
                .contents("설명1에서 2로 변경")
                .title("제목1에서 2로 변경")
                .build();
        return itemQuestionUpdateDto;
    }

    private static ItemQuestionSaveDto createItemQuestionSaveDto(Long itemId) {
        ItemQuestionSaveDto itemQuestionSaveDto = ItemQuestionSaveDto.builder()
                .itemId(itemId)
                .contents("설명1")
                .title("제목1")
                .member(null)
                .build();
        return itemQuestionSaveDto;
    }

    private Long createItem() {
        //기존에 카테고리가 먼저 생성되야함
        Category category1 = new Category("해양사고");
        Category category2 = new Category("중대사고");

        em.persist(category1);
        em.persist(category2);

        //ItemSaveDto 생성
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
        return itemId;
    }

}
