package com.safeking.shop.domain.item.domain.repository;

import com.safeking.shop.domain.item.domain.entity.ItemAnswer;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemAnswerRepository extends JpaRepository<ItemAnswer,Long> {


    Page<ItemAnswer> findByTargetId(Pageable pageable, Long itemQnaId);
}
