package com.safeking.shop.domain.item.web.controller;

import com.safeking.shop.domain.item.domain.service.CategoryService;
import com.safeking.shop.domain.item.domain.service.ItemQuestionService;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionUpdateDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionViewDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemUpdateDto;
import com.safeking.shop.domain.item.web.response.CategoryListResponse;
import com.safeking.shop.domain.item.web.response.ItemQuestionViewResponse;
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
public class ItemQuestionController {

    private final ItemQuestionService itemQuestionService;

    @PostMapping("/itemQna")
    public Long save(@RequestBody ItemQuestionSaveDto itemQuestionSaveDto, HttpServletRequest request){
        String username = TokenUtils.verify(request.getHeader(AUTH_HEADER).replace(BEARER, ""));
        itemQuestionSaveDto.setMemberId(username);
        return itemQuestionService.save(itemQuestionSaveDto);
    }

    @PutMapping("/itemQna/{itemQnaId}")
    public void update(@PathVariable Long itemQnaId, @RequestBody ItemQuestionUpdateDto itemQuestionUpdateDto){
        itemQuestionUpdateDto.setId(itemQnaId);
        itemQuestionService.update(itemQuestionUpdateDto);
    }

    @DeleteMapping("/itemQna/{itemQnaId}")
    public void delete(@PathVariable Long itemQnaId){
        itemQuestionService.delete(itemQnaId);
    }

    @GetMapping("/itemQna/{itemQnaId}")
    public ItemQuestionViewResponse view(@PathVariable Long itemQnaId){
        ItemQuestionViewDto itemQuestionViewDto = itemQuestionService.view(itemQnaId);
        ItemQuestionViewResponse itemQuestionViewResponse = new ItemQuestionViewResponse(
                itemQuestionViewDto.getId()
                , itemQuestionViewDto.getTitle()
                , itemQuestionViewDto.getContents()
                , itemQuestionViewDto.getItemId()
                , itemQuestionViewDto.getMemberId()
                , itemQuestionViewDto.getAnswer()
        );
        return itemQuestionViewResponse;
    }
}
