package com.safeking.shop.domain.notice.domain.sevice;

import com.safeking.shop.domain.notice.domain.entity.Notice;
import com.safeking.shop.domain.notice.domain.repository.NoticeRepository;
import com.safeking.shop.domain.notice.domain.sevice.servicedto.notice.NoticeSaveDto;
import com.safeking.shop.domain.notice.domain.sevice.servicedto.notice.NoticeUpdateDto;
import com.safeking.shop.domain.notice.domain.sevice.servicedto.notice.NoticeViewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void delete(Long id){
        Notice notice = noticeRepository.findById(id).orElseThrow();
        noticeRepository.delete(notice);
    }

    public NoticeViewDto view(Long id){
        Notice notice = noticeRepository.findById(id).orElseThrow();
        log.info(notice.getTitle());
        log.info(notice.getContents());
        return new NoticeViewDto(notice.getId()
                , notice.getTitle()
                , notice.getContents()
                , notice.getMemberId()
                , notice.getCreateDate().toString()
                , notice.getLastModifiedDate().toString());
    }

    public Page<Notice> list(Pageable pageable, String title){
        Page<Notice> posts = noticeRepository.findByTitleContaining(pageable, title);
        return posts;
    }

}
