package com.safeking.shop.domain.notice.domain.repository;

import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.notice.domain.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {

    Page<Notice> findByTitleContainingAndContentsContaining(Pageable pageable, String title, String contents);

    Page<Notice> findByTitleContaining(Pageable pageable, String title);
}
