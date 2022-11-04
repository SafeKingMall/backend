package com.safeking.shop.domain.item.domain.service;

import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.repository.CategoryRepository;
import com.safeking.shop.domain.item.domain.service.servicedto.category.CategorySaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.category.CategoryUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired CategoryService categoryService;

    @Autowired CategoryRepository categoryRepository;

    @Autowired EntityManager em;

    @Test
    @DisplayName("category 의 CUD 가 검증되야 한다.")
    void CUD() {
        //categorySaveDto 를 생성
        CategorySaveDto categorySaveDto = new CategorySaveDto("중대사고예방");

        //category save
        Long categoryId = categoryService.save(categorySaveDto);

        //SAVE 검증
        Category category = categoryRepository.findById(categoryId).orElseThrow();

        assertEquals("categorySaveDto 의 name 과 category 의 name 은 같아야한다.",
                categorySaveDto.getName(),category.getName());

        //categoryUpdateDto 를 생성
        CategoryUpdateDto categoryUpdateDto = new CategoryUpdateDto(categoryId, "해양사고예방");

        //category update
        categoryService.update(categoryUpdateDto);

        //UPDATE 검증
        assertEquals("categoryUpdateDto 의 id 와 category 의 id 는 같아야한다.",
                categoryUpdateDto.getId(),category.getId());

        assertEquals("categoryUpdateDto 의 name 과 category 의 name 은 같아야한다.",
                categoryUpdateDto.getName(),category.getName());

        //DELETE 실행
        categoryRepository.delete(category);

        //DELETE 검증
        assertThrows(NoSuchElementException.class,
                ()->categoryRepository.findById(categoryId).get());

    }
}