package com.safeking.shop.domain.item.domain.repository;


import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.entity.CategoryItem;
import com.safeking.shop.domain.item.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface CategoryItemRepository extends JpaRepository<CategoryItem,Long> {

    List <CategoryItem> findByCategory(Category category);
    @Modifying
    void deleteByItem(Item item);

    @Modifying
    void deleteByCategory(Category category);


}
