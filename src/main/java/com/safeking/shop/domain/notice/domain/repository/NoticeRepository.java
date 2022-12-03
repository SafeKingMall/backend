package com.safeking.shop.domain.notice.domain.repository;

import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.notice.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {

}
