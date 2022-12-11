package com.safeking.shop.domain.item.domain.service;

import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.repository.CategoryRepository;
import com.safeking.shop.domain.item.domain.service.servicedto.category.CategorySaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.category.CategoryUpdateDto;
import com.safeking.shop.domain.item.web.request.CategorySaveRequest;
import com.safeking.shop.domain.item.web.request.CategoryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Long save(CategorySaveRequest categorySaveRequest){

        Category category = Category.create(categorySaveRequest.getName(), categorySaveRequest.getSort());

        categoryRepository.save(category);

        return category.getId();

    }

    public void update(CategoryUpdateRequest categoryUpdateRequest){

        Category category = categoryRepository.findById(categoryUpdateRequest.getId()).orElseThrow();

        category.update(categoryUpdateRequest.getName(), categoryUpdateRequest.getSort());

    }

    public void delete(Long id){

        Category category = categoryRepository.findById(id).orElseThrow();

        categoryRepository.delete(category);

    }

    public Page<Category> list(Pageable pageable){
        return categoryRepository.findAll(pageable);
    }
}
