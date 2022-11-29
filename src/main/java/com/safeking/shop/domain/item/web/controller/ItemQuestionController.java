package com.safeking.shop.domain.item.web.controller;

import com.safeking.shop.domain.item.domain.service.CategoryService;
import com.safeking.shop.domain.item.domain.service.ItemQuestionService;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
import com.safeking.shop.domain.item.web.response.CategoryListResponse;
import com.safeking.shop.global.jwt.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static com.safeking.shop.global.jwt.TokenUtils.BEARER;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemQuestionController {

    private final ItemQuestionService itemQuestionService;

    @PostMapping("/itemQna")
    public Long save(@RequestBody ItemQuestionSaveDto itemQuestionSaveDto, HttpServletRequest request){
        String username = TokenUtils.verify(request.getHeader(AUTH_HEADER).replace(BEARER, ""));
        itemQuestionSaveDto.setMemberId(username);
        return itemQuestionService.save(itemQuestionSaveDto);
    }
}
