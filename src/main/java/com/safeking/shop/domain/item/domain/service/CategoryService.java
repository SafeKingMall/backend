package com.safeking.shop.domain.item.domain.service;

import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.repository.CategoryItemRepository;
import com.safeking.shop.domain.item.domain.repository.CategoryRepository;
import com.safeking.shop.domain.item.domain.service.servicedto.category.CategorySaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.category.CategoryUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryItemRepository categoryItemRepository;

    private final CategoryRepository categoryRepository;

    public Long save(CategorySaveDto categorySaveDto){

        Category category = new Category(categorySaveDto.getName());

        categoryRepository.save(category);

        return category.getId();

    }

    public void update(CategoryUpdateDto categoryUpdateDto){

        Category category = categoryRepository.findById(categoryUpdateDto.getId()).orElseThrow();

        category.updateName(categoryUpdateDto.getName());

    }

    public void delete(Long id){

        Category category = categoryRepository.findById(id).orElseThrow();

        //CategoryItem을 먼저 삭제
        categoryItemRepository.deleteByCategory(category);

        categoryRepository.delete(category);

    }
}
