package com.safeking.shop.domain.item.domain.repository;

import com.safeking.shop.domain.item.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> {
}
