package com.safeking.shop.domain.item.web.controller;

import com.safeking.shop.domain.item.domain.service.ItemAnswerService;
import com.safeking.shop.domain.item.domain.service.ItemQuestionService;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer.ItemAnswerSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionUpdateDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionViewDto;
import com.safeking.shop.domain.item.web.query.repository.ItemAnswerQueryRepository;
import com.safeking.shop.domain.item.web.request.ItemAnswerSaveRequest;
import com.safeking.shop.domain.item.web.response.ItemAnswerListResponse;
import com.safeking.shop.domain.item.web.response.ItemQuestionListResponse;
import com.safeking.shop.domain.item.web.response.ItemQuestionViewResponse;
import com.safeking.shop.global.jwt.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static com.safeking.shop.global.jwt.TokenUtils.BEARER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class ItemAnswerController {

    private final ItemAnswerService itemAnswerService;

    @GetMapping("user/itemAnswer/list/{itemQnaId}")
    public Page<ItemAnswerListResponse> list(@PathVariable Long itemQnaId, @PageableDefault(size=1000) Pageable pageable){
        Page<ItemAnswerListResponse> list = itemAnswerService.list(itemQnaId, pageable).map(m->ItemAnswerListResponse.builder()
                .id(m.getId())
                .contents(m.getContents())
                .memberId(m.getMemberId())
                .itemQnaId(m.getItemQnaId())
                .createDate(m.getCreateDate())
                .lastModifiedDate(m.getLastModifiedDate())
                .build());
        return list;
    }

    @PostMapping("user/itemAnswer")
    public Long save(@RequestBody ItemAnswerSaveRequest itemAnswerSaveRequest){
        Long id = itemAnswerService.save(itemAnswerSaveRequest);
        return id;
    }

    @PutMapping("user/itemAnswer/{itemAnswerId}")
    public void update(@PathVariable Long itemAnswerId,@RequestBody ItemAnswerSaveRequest itemAnswerSaveRequest){
        itemAnswerSaveRequest.setId(itemAnswerId);
        itemAnswerService.update(itemAnswerSaveRequest);
    }

    @DeleteMapping("user/itemAnswer/{itemAnswerId}")
    public void delete(@PathVariable Long itemAnswerId){
        itemAnswerService.delete(itemAnswerId);
    }
}
