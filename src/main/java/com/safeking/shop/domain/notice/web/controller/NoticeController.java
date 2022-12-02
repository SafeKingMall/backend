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
import com.safeking.shop.global.jwt.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static com.safeking.shop.global.jwt.TokenUtils.BEARER;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/admin/notice")
    public Long save(NoticeSaveDto noticeSaveDto, HttpServletRequest request){
        String username = TokenUtils.verify(request.getHeader(AUTH_HEADER).replace(BEARER, ""));
        noticeSaveDto.setMemberId(username);
        return noticeService.save(noticeSaveDto);
    }

    @PutMapping("/admin/notice/{noticeId}")
    public void update(@PathVariable Long noticeId, @RequestBody NoticeUpdateDto noticeUpdateDto, HttpServletRequest request){
        String username = TokenUtils.verify(request.getHeader(AUTH_HEADER).replace(BEARER, ""));
        noticeUpdateDto.setMemberId(username);
        noticeUpdateDto.setId(noticeId);
        noticeService.update(noticeUpdateDto);
    }

    @DeleteMapping("/admin/notice/{noticeId}")
    public void delete(@PathVariable Long noticeId, HttpServletRequest request){
        String username = TokenUtils.verify(request.getHeader(AUTH_HEADER).replace(BEARER, ""));
        noticeService.delete(noticeId);
    }

}
