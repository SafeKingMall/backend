package com.safeking.shop.domain.item.domain.repository;

import com.safeking.shop.domain.item.domain.entity.ItemAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemAnswerRepository extends JpaRepository<ItemAnswer,Long> {

}
