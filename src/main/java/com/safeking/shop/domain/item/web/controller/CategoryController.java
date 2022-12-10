package com.safeking.shop.domain.item.web.controller;

import com.safeking.shop.domain.item.domain.service.CategoryService;
import com.safeking.shop.domain.item.web.response.CategoryListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("category/list")
    public Page<CategoryListResponse> categoryList(@PageableDefault(size=10) Pageable pageable){
        Page<CategoryListResponse> list = categoryService.list(pageable).map(m -> CategoryListResponse.builder()
                .id(m.getId())
                .name(m.getName())
                .createDate(m.getCreateDate().toString())
                .lastModifiedDate(m.getLastModifiedDate().toString())
                .build()
        );
        return list;
    }
}
