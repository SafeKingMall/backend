//package com.safeking.shop.domain.item.domain.service;
//
//import com.safeking.shop.domain.item.domain.entity.Category;
//import com.safeking.shop.domain.item.domain.entity.ItemAnswer;
//import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
//import com.safeking.shop.domain.item.domain.repository.ItemAnswerRepository;
//import com.safeking.shop.domain.item.domain.repository.ItemQuestionRepository;
//import com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer.ItemAnswerSaveDto;
//import com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer.ItemAnswerUpdateDto;
//import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionSaveDto;
//import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@Transactional
//class ItemAnswerServiceTest {
//
//    @Autowired ItemQuestionService itemQuestionService;
//    @Autowired ItemService itemService;
//    @Autowired EntityManager em;
//    @Autowired ItemAnswerService itemAnswerService;
//    @Autowired ItemAnswerRepository itemAnswerRepository;
//
//    @Autowired ItemQuestionRepository itemQuestionRepository;
//
//    @Test
//    @DisplayName("itemAnswer 의 CUD 기능을 검증해야한다.")
//    void CUD() {
//        //ItemQuestion 생성
//        Long itemQuestionId = createItemQuestion();
//
//        //itemAnswerSaveDto 생성
//        ItemAnswerSaveDto itemAnswerSaveDto = createItemAnswerSaveDto(itemQuestionId);
//
//        //itemAnswer SAVE
//        Long itemAnswerId = itemAnswerService.save(itemAnswerSaveDto);
//
//        //SAVE 검증
//        ItemAnswer itemAnswer = itemAnswerRepository.findById(itemAnswerId).orElseThrow();
//
//        assertEquals("itemAnswerSaveDto 의 ItemQuestionId 와 itemAnswer 의 ItemQuestionId 는 같아야한다.",
//                itemAnswerSaveDto.getItemQuestionId(),itemAnswer.getTarget().getId());
//        assertEquals("itemAnswerSaveDto 의 Contents 와 itemAnswer 의 Contents 는 같아야한다.",
//                itemAnswerSaveDto.getContents(),itemAnswer.getContents());
//
//        //itemAnswerUpdateDto 생성로직
//        ItemAnswerUpdateDto itemAnswerUpdateDto = createItemAnswerUpdateDto(itemAnswerId);
//
//        //UPDATE
//        itemAnswerService.update(itemAnswerUpdateDto);
//
//        //UPDATE 검증
//        assertEquals("itemAnswerUpdateDto 의 ItemQuestionId 와 itemAnswer 의 ItemQuestionId 는 같아야한다.",
//                itemAnswerUpdateDto.getId(),itemAnswer.getId());
//        assertEquals("itemAnswerUpdateDto 의 Contents 와 itemAnswer 의 Contents 는 같아야한다.",
//                itemAnswerUpdateDto.getContents(),itemAnswer.getContents());
//
//        //DELETE
//        itemQuestionService.delete(itemAnswerId);
//
//        //DELETE 검증
//        assertThrows(NoSuchElementException.class,
//                ()->itemAnswerRepository.findById(itemAnswerId).get());
//    }
//
//    @Test
//    @DisplayName("itemQuestion 이 삭제시에 itemAnswer 는 삭제되어야한다.")
//    public void delete(){
//        //ItemQuestion 생성
//        Long itemQuestionId = createItemQuestion();
//
//        //itemAnswerSaveDto 생성
//        ItemAnswerSaveDto itemAnswerSaveDto = createItemAnswerSaveDto(itemQuestionId);
//
//        //itemAnswer SAVE
//        Long itemAnswerId = itemAnswerService.save(itemAnswerSaveDto);
//
//        //itemQuestion 을 삭제
//        ItemQuestion itemQuestion = itemQuestionRepository.findById(itemQuestionId).orElseThrow();
//
//        itemQuestionRepository.delete(itemQuestion);
//
//        //itemQuestion 이 삭제가 되었는지 검증
//        assertThrows(NoSuchElementException.class,
//                ()->itemQuestionRepository.findById(itemQuestionId).get());
//
//        //itemAnswer 이 삭제가 되었는지 검증
//        assertThrows(NoSuchElementException.class,
//                ()->itemAnswerRepository.findById(itemAnswerId).get());
//    }
//
//    private static ItemAnswerUpdateDto createItemAnswerUpdateDto(Long itemAnswerId) {
//        ItemAnswerUpdateDto itemAnswerUpdateDto = ItemAnswerUpdateDto.builder()
//                .id(itemAnswerId)
//                .contents("설명1에서 설명2로 변환")
//                .build();
//        return itemAnswerUpdateDto;
//    }
//
//    private ItemAnswerSaveDto createItemAnswerSaveDto(Long itemQuestionId) {
//        return ItemAnswerSaveDto.builder()
//                .itemQuestionId(itemQuestionId)
//                .admin(null)
//                .contents("설명1")
//                .build();
//    }
//
//    private Long createItemQuestion() {
//        //아이템을 먼저 생성
//        //기존에 카테고리가 먼저 생성되야함
//        Category category1 = new Category("해양사고");
//        Category category2 = new Category("중대사고");
//
//        em.persist(category1);
//        em.persist(category2);
//
//        //ItemSaveDto 생성
//        List<Long> categories = new ArrayList<>();
//
//        categories.add(category1.getId());
//
//        categories.add(category2.getId());
//
//        ItemSaveDto itemSaveDto = ItemSaveDto.builder()
//                .name("안전모")
//                .quantity(100)
//                .description("설명1")
//                .adminId(null)
//                .categories(categories)
//                .price(1000)
//                .build();
//
//        //Item 생성
//        Long itemId = itemService.save(itemSaveDto);
//
//        //ItemQuestionSaveDto 생성
//        ItemQuestionSaveDto itemQuestionSaveDto = ItemQuestionSaveDto.builder()
//                .itemId(itemId)
//                .contents("설명1")
//                .title("제목1")
//                .member(null)
//                .build();
//
//        Long itemQuestionId = itemQuestionService.save(itemQuestionSaveDto);
//
//        return itemQuestionId;
//    }
//}