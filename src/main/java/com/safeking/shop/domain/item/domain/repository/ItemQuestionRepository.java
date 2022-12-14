package com.safeking.shop.domain.item.domain.repository;

import com.querydsl.core.group.GroupBy;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ItemQuestionRepository extends JpaRepository<ItemQuestion,Long> {


    @Override
    @Query("select iq from ItemQuestion iq join fetch iq.writer where iq.id=:id")
    Optional<ItemQuestion> findById(@Param("id") Long id);

    Page<ItemQuestion> findByTitleContaining(Pageable pageable, String title);
}
