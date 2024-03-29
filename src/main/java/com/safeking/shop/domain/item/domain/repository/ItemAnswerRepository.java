package com.safeking.shop.domain.item.domain.repository;

import com.safeking.shop.domain.item.domain.entity.ItemAnswer;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<ItemAnswer> findByTargetId(Pageable pageable, Long itemQnaId);
    List<ItemAnswer> findByMember(Member member);

    @Modifying
    @Query("delete from ItemAnswer ia where ia in :itemAnswerList")
    void deleteByBatch(@Param("itemAnswerList") List<ItemAnswer> itemAnswerList);
}
