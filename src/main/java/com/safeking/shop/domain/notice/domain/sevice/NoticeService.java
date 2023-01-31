package com.safeking.shop.domain.notice.domain.sevice;

import com.safeking.shop.domain.notice.domain.entity.Notice;
import com.safeking.shop.domain.notice.domain.repository.NoticeRepository;
import com.safeking.shop.domain.notice.domain.sevice.servicedto.notice.NoticeRownumDto;
import com.safeking.shop.domain.notice.domain.sevice.servicedto.notice.NoticeSaveDto;
import com.safeking.shop.domain.notice.domain.sevice.servicedto.notice.NoticeUpdateDto;
import com.safeking.shop.domain.notice.domain.sevice.servicedto.notice.NoticeViewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public Page<Notice> listAndTitle(Pageable pageable, String title){
        Page<Notice> posts = null;

        posts = noticeRepository.findByTitleContaining(pageable, title);
        return posts;
    }

    public Page<Notice> listAndCreateDate(Pageable pageable, LocalDateTime startDateTime, LocalDateTime endDateTime){
        Page<Notice> posts = null;

        posts = noticeRepository.findByCreateDateBetween(pageable, startDateTime, endDateTime);
        return posts;
    }

    public Page<Notice> list(Pageable pageable){
        Page<Notice> posts = null;

        posts = noticeRepository.findAll(pageable);
        return posts;
    }

    public NoticeViewDto viewPrev(Long id, String title, String createDate, Pageable pageable){
        System.out.println("id : "+id);
        System.out.println("title : "+title);
        System.out.println("createDate : "+createDate);
        List<NoticeRownumDto> list = null;
        if(title != null){
            list = noticeRepository.findRownumByIdAndTitleContaining(title, pageable);
        }else if(createDate != null){
            list = noticeRepository.findRownumByIdAndCreateDateBetween(createDate, pageable);
        }else{
            list = noticeRepository.findRownumById(pageable);
        }

        Integer rownum = null;
        for(NoticeRownumDto a : list){
            System.out.println("getId : "+a.getId());
            System.out.println("getTitle : "+a.getTitle());
            System.out.println("getRownum : "+a.getRownum());
            if(id.equals(a.getId())){
                rownum = a.getRownum();
                System.out.println("rownum : "+a.getRownum());
            }
        }
        System.out.println("rownum : "+rownum);
        if(rownum == null) return null;
        rownum -= 1;
        for(NoticeRownumDto a : list){
            if(a.getRownum() == rownum){
                return new NoticeViewDto(a.getId()
                        , a.getTitle()
                        , a.getContents()
                        , a.getMemberId()
                        , a.getCreateDate().toString()
                        , a.getLastModifiedDate().toString());
            }
        }
        return null;
    }

    public NoticeViewDto viewNext(Long id, String title, String createDate, Pageable pageable){
        System.out.println("id : "+id);
        System.out.println("title : "+title);
        System.out.println("createDate : "+createDate);
        List<NoticeRownumDto> list = null;
        if(title != null){
            list = noticeRepository.findRownumByIdAndTitleContaining(title, pageable);
        }else if(createDate != null){
            list = noticeRepository.findRownumByIdAndCreateDateBetween(createDate, pageable);
        }else{
            list = noticeRepository.findRownumById(pageable);
        }

        Integer rownum = null;
        for(NoticeRownumDto a : list){
            System.out.println("getId : "+a.getId());
            System.out.println("getTitle : "+a.getTitle());
            System.out.println("getRownum : "+a.getRownum());
            if(id.equals(a.getId())){
                rownum = a.getRownum();
                System.out.println("rownum : "+a.getRownum());
            }
        }
        System.out.println("rownum : "+rownum);
        if(rownum == null) return null;
        rownum += 1;
        for(NoticeRownumDto a : list){
            if(a.getRownum() == rownum){
                return new NoticeViewDto(a.getId()
                        , a.getTitle()
                        , a.getContents()
                        , a.getMemberId()
                        , a.getCreateDate().toString()
                        , a.getLastModifiedDate().toString());
            }
        }
        return null;

    }

}
