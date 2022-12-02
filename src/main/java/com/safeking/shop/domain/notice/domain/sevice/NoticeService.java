package com.safeking.shop.domain.notice.domain.sevice;

import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.entity.CategoryItem;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.CategoryItemRepository;
import com.safeking.shop.domain.item.domain.repository.CategoryRepository;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemUpdateDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemViewDto;
import com.safeking.shop.domain.notice.domain.entity.Notice;
import com.safeking.shop.domain.notice.domain.repository.NoticeRepository;
import com.safeking.shop.domain.notice.domain.sevice.servicedto.notice.NoticeSaveDto;
import com.safeking.shop.domain.notice.domain.sevice.servicedto.notice.NoticeUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Long save(NoticeSaveDto noticeSaveDto){
        Notice notice = Notice.create(noticeSaveDto.getTitle(), noticeSaveDto.getContents(), noticeSaveDto.getMemberId());
        noticeRepository.save(notice);
        return notice.getId();
    }

    public void update(NoticeUpdateDto noticeUpdateDto){
        Notice notice = noticeRepository.findById(noticeUpdateDto.getId()).orElseThrow();
        notice.update(noticeUpdateDto.getTitle(), noticeUpdateDto.getContents(), noticeUpdateDto.getMemberId());
    }

}
