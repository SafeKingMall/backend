package com.safeking.shop.domain.item.domain.repository;

import com.querydsl.core.group.GroupBy;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import com.safeking.shop.domain.notice.domain.entity.Notice;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemQuestionRepository extends JpaRepository<ItemQuestion,Long> {


    @Override
    @Query("select iq from ItemQuestion iq join fetch iq.member where iq.id=:id")
    Optional<ItemQuestion> findById(@Param("id") Long id);

    Page<ItemQuestion> findByTitleContaining(Pageable pageable, String title);

    Page<ItemQuestion> findByMemberUsernameContaining(Pageable pageable, String memberId);

    Page<ItemQuestion> findByCreateDateBetween(Pageable pageable, LocalDateTime startDateTime, LocalDateTime endDateTime);

    ItemQuestion findByIdAndPassword(Long id, String password);

    @Modifying
    @Query("delete from ItemQuestion iq where iq in :questionList")
    void deleteByTargetBatch(@Param("questionList") List<ItemQuestion> questionList);

    List<ItemQuestion> findByMember(Member member);
}
