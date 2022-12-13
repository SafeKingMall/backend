package com.safeking.shop.domain.item.web.controller;

import com.safeking.shop.domain.item.domain.service.CategoryService;
import com.safeking.shop.domain.item.web.request.CategorySaveRequest;
import com.safeking.shop.domain.item.web.request.CategoryUpdateRequest;
import com.safeking.shop.domain.item.web.response.CategoryListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category/list")
    public Page<CategoryListResponse> categoryList(@PageableDefault(size=10) Pageable pageable){
        Page<CategoryListResponse> list = categoryService.list(pageable).map(m -> CategoryListResponse.builder()
                .id(m.getId())
                .name(m.getName())
                .createDate(m.getCreateDate().toString())
                .lastModifiedDate(m.getLastModifiedDate().toString())
                .sort(m.getSort())
                .build()
        );
        return list;
    }

    @GetMapping("admin/category/list")
    public Page<CategoryListResponse> adminList(@PageableDefault(size=10) Pageable pageable){
        Page<CategoryListResponse> list = categoryService.list(pageable).map(m -> CategoryListResponse.builder()
                .id(m.getId())
                .name(m.getName())
                .createDate(m.getCreateDate().toString())
                .lastModifiedDate(m.getLastModifiedDate().toString())
                .sort(m.getSort())
                .build()
        );
        return list;
    }

    @PostMapping("admin/category")
    public Long save(@RequestBody CategorySaveRequest categorySaveRequest){
        return categoryService.save(categorySaveRequest);
    }

    @PutMapping("admin/category/{categoryId}")
    public void update(@PathVariable Long categoryId, @RequestBody CategoryUpdateRequest categoryUpdateRequest){
        categoryUpdateRequest.setId(categoryId);
        categoryService.update(categoryUpdateRequest);
    }

    @DeleteMapping("admin/category/{categoryId}")
    public void delete(@PathVariable Long categoryId){
        categoryService.delete(categoryId);
    }
}
