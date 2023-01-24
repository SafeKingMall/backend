package com.safeking.shop.domain.notice.domain.repository;

import com.safeking.shop.domain.notice.domain.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface NoticeRepository extends JpaRepository<Notice,Long> {

    Page<Notice> findByTitleContainingAndContentsContaining(Pageable pageable, String title, String contents);

    Page<Notice> findByTitleContaining(Pageable pageable, String title);


    Page<Notice> findByCreateDateBetween(Pageable pageable, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
