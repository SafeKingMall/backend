package com.safeking.shop.domain.item.domain.repository;

import com.querydsl.core.group.GroupBy;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.web.response.ItemListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> {

    Page<Item> findByNameContainingAndCategoryNameContaining(Pageable pageable, String itemName, String categoryName);
}
