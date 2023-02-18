package com.safeking.shop.domain.notice.web.controller;

import com.nimbusds.oauth2.sdk.Request;
import com.safeking.shop.domain.item.domain.service.ItemService;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemUpdateDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemViewDto;
import com.safeking.shop.domain.item.web.response.ItemListResponse;
import com.safeking.shop.domain.item.web.response.ItemViewResponse;
import com.safeking.shop.domain.notice.domain.sevice.NoticeService;
import com.safeking.shop.domain.notice.domain.sevice.servicedto.notice.NoticeSaveDto;
import com.safeking.shop.domain.notice.domain.sevice.servicedto.notice.NoticeUpdateDto;
import com.safeking.shop.domain.notice.domain.sevice.servicedto.notice.NoticeViewDto;
import com.safeking.shop.domain.notice.web.response.NoticeListResponse;
import com.safeking.shop.domain.notice.web.response.NoticeViewResponse;
import com.safeking.shop.global.jwt.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static com.safeking.shop.global.jwt.TokenUtils.BEARER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("admin/notice")
    public Long save(@RequestBody NoticeSaveDto noticeSaveDto, HttpServletRequest request){
        String username = TokenUtils.verify(request.getHeader(AUTH_HEADER).replace(BEARER, ""));
        noticeSaveDto.setMemberId(username);
        return noticeService.save(noticeSaveDto);
    }

    @PutMapping("admin/notice/{noticeId}")
    public void update(@PathVariable Long noticeId, @RequestBody NoticeUpdateDto noticeUpdateDto, HttpServletRequest request){
        String username = TokenUtils.verify(request.getHeader(AUTH_HEADER).replace(BEARER, ""));
        noticeUpdateDto.setMemberId(username);
        noticeUpdateDto.setId(noticeId);
        noticeService.update(noticeUpdateDto);
    }

    @DeleteMapping("admin/notice/{noticeId}")
    public void delete(@PathVariable Long noticeId, HttpServletRequest request){
        String username = TokenUtils.verify(request.getHeader(AUTH_HEADER).replace(BEARER, ""));
        noticeService.delete(noticeId);
    }

    @GetMapping("admin/notice/{noticeId}")
    public NoticeViewResponse adminView(@PathVariable Long noticeId){
        NoticeViewDto noticeViewDto = noticeService.view(noticeId);
        return new NoticeViewResponse(noticeViewDto.getId(),
                noticeViewDto.getTitle(),
                noticeViewDto.getContents(),
                noticeViewDto.getMemberId(),
                noticeViewDto.getCreateDate(),
                noticeViewDto.getLastModifiedDate()
        );
    }

    @GetMapping("admin/notice/list")
    public Page<NoticeListResponse> adminList(Pageable pageable, @RequestParam(required = false, defaultValue = "") String title
                                              , @RequestParam(required = false, defaultValue = "") String createDate
            ){
        Page<NoticeListResponse> page = null;
        if(!"".equals(title)){
            page = noticeService.listAndTitle(pageable, title).map(m->NoticeListResponse.builder()
                    .id(m.getId())
                    .title(m.getTitle())
                    .memberId(m.getMemberId())
                    .createDate(m.getCreateDate().toString())
                    .lastModifiedDate(m.getLastModifiedDate().toString())
                    .build()
            );
        }else if(!"".equals(createDate)){
            LocalDateTime s1 = null;
            LocalDateTime s2 = null;
            if(createDate.length() == 10){
                s1 = LocalDateTime.parse(createDate+" 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                s2 = LocalDateTime.parse(createDate+" 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }else if(createDate.length() == 4){
                s1 = LocalDateTime.parse(createDate+"-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                s2 = LocalDateTime.parse(createDate+"-12-31 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }else{
                s1 = LocalDateTime.parse(createDate+"-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                s2 = LocalDateTime.parse(createDate+"-31 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            log.debug(s1.toString());
            log.debug(s2.toString());
            page = noticeService.listAndCreateDate(pageable, s1, s2).map(m->NoticeListResponse.builder()
                    .id(m.getId())
                    .title(m.getTitle())
                    .memberId(m.getMemberId())
                    .createDate(m.getCreateDate().toString())
                    .lastModifiedDate(m.getLastModifiedDate().toString())
                    .build()
            );
        }else {
            page = noticeService.list(pageable).map(m -> NoticeListResponse.builder()
                    .id(m.getId())
                    .title(m.getTitle())
                    .memberId(m.getMemberId())
                    .createDate(m.getCreateDate().toString())
                    .lastModifiedDate(m.getLastModifiedDate().toString())
                    .build()
            );
        }
        return page;
    }

    @GetMapping("notice/{noticeId}")
    public NoticeViewResponse view(@PathVariable Long noticeId){
        NoticeViewDto noticeViewDto = noticeService.view(noticeId);
        return new NoticeViewResponse(noticeViewDto.getId(),
                noticeViewDto.getTitle(),
                noticeViewDto.getContents(),
                noticeViewDto.getMemberId(),
                noticeViewDto.getCreateDate(),
                noticeViewDto.getLastModifiedDate()
        );
    }

    @GetMapping("notice/list")
    public Page<NoticeListResponse> list(Pageable pageable, @RequestParam(required = false, defaultValue = "") String title
            , @RequestParam(required = false, defaultValue = "") String createDate
            ){
        Page<NoticeListResponse> page = null;

        if(!"".equals(title)){
            page = noticeService.listAndTitle(pageable, title).map(m->NoticeListResponse.builder()
                    .id(m.getId())
                    .title(m.getTitle())
                    .memberId(m.getMemberId())
                    .createDate(m.getCreateDate().toString())
                    .lastModifiedDate(m.getLastModifiedDate().toString())
                    .build()
            );
        }else if(!"".equals(createDate)){
            LocalDateTime s1 = null;
            LocalDateTime s2 = null;
            if(createDate.length() == 10){
                s1 = LocalDateTime.parse(createDate+" 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                s2 = LocalDateTime.parse(createDate+" 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }else if(createDate.length() == 4){
                s1 = LocalDateTime.parse(createDate+"-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                s2 = LocalDateTime.parse(createDate+"-12-31 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }else{
                s1 = LocalDateTime.parse(createDate+"-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                s2 = LocalDateTime.parse(createDate+"-31 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }

            log.debug(s1.toString());
            log.debug(s2.toString());
            page = noticeService.listAndCreateDate(pageable, s1, s2).map(m->NoticeListResponse.builder()
                    .id(m.getId())
                    .title(m.getTitle())
                    .memberId(m.getMemberId())
                    .createDate(m.getCreateDate().toString())
                    .lastModifiedDate(m.getLastModifiedDate().toString())
                    .build()
            );
        }else{
            page = noticeService.list(pageable).map(m->NoticeListResponse.builder()
                    .id(m.getId())
                    .title(m.getTitle())
                    .memberId(m.getMemberId())
                    .createDate(m.getCreateDate().toString())
                    .lastModifiedDate(m.getLastModifiedDate().toString())
                    .build()
            );
        }


        return page;
    }

    @GetMapping("admin/notice/prev/{noticeId}")
    public NoticeViewResponse adminViewPrev(@PathVariable Long noticeId, @RequestParam(required = false) String title
            , @RequestParam(required = false) String createDate, @PageableDefault(size=1000000) Pageable pageable){
        NoticeViewDto noticeViewDto = noticeService.viewPrev(noticeId, title, createDate, pageable);
        if(noticeViewDto == null) return null;
        return new NoticeViewResponse(noticeViewDto.getId(),
                noticeViewDto.getTitle(),
                noticeViewDto.getContents(),
                noticeViewDto.getMemberId(),
                noticeViewDto.getCreateDate(),
                noticeViewDto.getLastModifiedDate()
        );
    }

    @GetMapping("admin/notice/next/{noticeId}")
    public NoticeViewResponse adminViewNext(@PathVariable Long noticeId, @RequestParam(required = false) String title
            , @RequestParam(required = false) String createDate, @PageableDefault(size=1000000) Pageable pageable){
        NoticeViewDto noticeViewDto = noticeService.viewNext(noticeId, title, createDate, pageable);
        if(noticeViewDto == null) return null;
        return new NoticeViewResponse(noticeViewDto.getId(),
                noticeViewDto.getTitle(),
                noticeViewDto.getContents(),
                noticeViewDto.getMemberId(),
                noticeViewDto.getCreateDate(),
                noticeViewDto.getLastModifiedDate()
        );
    }

    @GetMapping("notice/prev/{noticeId}")
    public NoticeViewResponse viewPrev(@PathVariable Long noticeId, @RequestParam(required = false) String title
            , @RequestParam(required = false) String createDate, @PageableDefault(size=1000000) Pageable pageable){
        NoticeViewDto noticeViewDto = noticeService.viewPrev(noticeId, title, createDate, pageable);
        if(noticeViewDto == null) return null;
        return new NoticeViewResponse(noticeViewDto.getId(),
                noticeViewDto.getTitle(),
                noticeViewDto.getContents(),
                noticeViewDto.getMemberId(),
                noticeViewDto.getCreateDate(),
                noticeViewDto.getLastModifiedDate()
        );
    }

    @GetMapping("notice/next/{noticeId}")
    public NoticeViewResponse viewNext(@PathVariable Long noticeId, @RequestParam(required = false) String title
            , @RequestParam(required = false) String createDate, @PageableDefault(size=1000000) Pageable pageable){
        NoticeViewDto noticeViewDto = noticeService.viewNext(noticeId, title, createDate, pageable);
        if(noticeViewDto == null) return null;
        return new NoticeViewResponse(noticeViewDto.getId(),
                noticeViewDto.getTitle(),
                noticeViewDto.getContents(),
                noticeViewDto.getMemberId(),
                noticeViewDto.getCreateDate(),
                noticeViewDto.getLastModifiedDate()
        );
    }
}
