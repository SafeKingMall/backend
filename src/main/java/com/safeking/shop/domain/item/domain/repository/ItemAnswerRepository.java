package com.safeking.shop.domain.item.domain.repository;

import com.safeking.shop.domain.item.domain.entity.ItemAnswer;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemAnswerRepository extends JpaRepository<ItemAnswer,Long> {
    @Modifying
    @Query("delete from ItemAnswer ia where ia.target in :questionList")
    void deleteByTargetBatch(@Param("questionList") List<ItemQuestion> questionList);

}
