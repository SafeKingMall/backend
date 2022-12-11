package com.safeking.shop.domain.item.domain.repository;

import com.safeking.shop.domain.item.domain.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

}
