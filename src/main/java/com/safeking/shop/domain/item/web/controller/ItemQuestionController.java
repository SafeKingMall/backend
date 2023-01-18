package com.safeking.shop.domain.item.web.controller;

import com.safeking.shop.domain.item.domain.service.CategoryService;
import com.safeking.shop.domain.item.domain.service.ItemQuestionService;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionUpdateDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionViewDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemUpdateDto;
import com.safeking.shop.domain.item.web.response.CategoryListResponse;
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

import java.util.List;

import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static com.safeking.shop.global.jwt.TokenUtils.BEARER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class ItemQuestionController {

    private final ItemQuestionService itemQuestionService;

    @PostMapping("user/itemQna")
    public Long save(@RequestBody ItemQuestionSaveDto itemQuestionSaveDto, HttpServletRequest request){
        String username = TokenUtils.verify(request.getHeader(AUTH_HEADER).replace(BEARER, ""));
        itemQuestionSaveDto.setMemberId(username);
        return itemQuestionService.save(itemQuestionSaveDto);
    }

    @PutMapping("user/itemQna/{itemQnaId}")
    public void update(@PathVariable Long itemQnaId, @RequestBody ItemQuestionUpdateDto itemQuestionUpdateDto,HttpServletRequest request){
        itemQuestionUpdateDto.setId(itemQnaId);
        itemQuestionService.update(itemQuestionUpdateDto,TokenUtils.getUsername(request));
    }

    @DeleteMapping("user/itemQna/{itemQnaId}")
    public void delete(@PathVariable Long itemQnaId,HttpServletRequest request){
        itemQuestionService.delete(itemQnaId,TokenUtils.getUsername(request));
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

    @GetMapping("/itemQna/list")
    public Page<ItemQuestionListResponse> list(@PageableDefault(size=10) Pageable pageable, @RequestParam(required = false, defaultValue = "") String title){
        return itemQuestionService.list(pageable, title).map(m -> ItemQuestionListResponse.builder()
                .id(m.getId())
                .itemId(m.getItemId())
                .title(m.getTitle())
                .createDate(m.getCreateDate())
                .lastModifiedDate(m.getLastModifiedDate())
                .memberId(m.getMemberId())
                .build()
        );
    }
}
