package com.safeking.shop.domain.item.domain.repository;

import com.querydsl.core.group.GroupBy;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.web.response.ItemListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long> {

    Page<Item> findByNameContainingAndCategoryNameContaining(Pageable pageable, String itemName, String categoryName);

    ArrayList<Item> findByCategoryId(Long id);

    Page<Item> findByNameContaining(Pageable pageable, String itemName);

    Page<Item> findByCategoryNameContaining(Pageable pageable, String categoryName);
}
