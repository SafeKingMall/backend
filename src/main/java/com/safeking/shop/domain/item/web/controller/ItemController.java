package com.safeking.shop.domain.item.web.controller;

import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.service.ItemService;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemUpdateDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemViewDto;
import com.safeking.shop.domain.item.web.request.ItemRequest;
import com.safeking.shop.domain.item.web.response.ItemListResponse;
import com.safeking.shop.domain.item.web.response.ItemResponse;
import com.safeking.shop.domain.item.web.response.ItemViewResponse;
import com.safeking.shop.global.jwt.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static com.safeking.shop.global.jwt.TokenUtils.BEARER;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/item/admin")
    public Long save(@RequestBody ItemSaveDto itemSaveDto, HttpServletRequest request){
        String username = TokenUtils.verify(request.getHeader(AUTH_HEADER).replace(BEARER, ""));
        itemSaveDto.setAdminId(username);
        return itemService.save(itemSaveDto);
    }

    @PutMapping("/item/admin/{itemId}")
    public void update(@PathVariable Long itemId, @RequestBody ItemUpdateDto itemUpdateDto, HttpServletRequest request){
        String username = TokenUtils.verify(request.getHeader(AUTH_HEADER).replace(BEARER, ""));
        itemUpdateDto.setAdminId(username);
        itemUpdateDto.setId(itemId);
        itemService.update(itemUpdateDto);
    }

    @DeleteMapping("/item/admin/{itemId}")
    public void delete(@PathVariable Long itemId){
        itemService.delete(itemId);
    }

    @GetMapping("/item/admin/{itemId}")
    public ItemViewResponse itemView(@PathVariable Long itemId){
        ItemViewResponse itemViewResponse;
        itemViewResponse = new ItemViewResponse(itemService.view(itemId).getId()
                , itemService.view(itemId).getName()
                , itemService.view(itemId).getQuantity()
                , itemService.view(itemId).getDescription()
                , itemService.view(itemId).getPrice()
                , itemService.view(itemId).getAdminId()
                , itemService.view(itemId).getCategories()
                , itemService.view(itemId).getCategoryName()
                , itemService.view(itemId).getCreateDate()
                , itemService.view(itemId).getLastModifiedDate()
        );
        return itemViewResponse;
    }

    @GetMapping("/item/admin/list")
    public Page<ItemListResponse> itemList(@PageableDefault(size=10)Pageable pageable){
        Page<ItemListResponse> itemLst = itemService.List(pageable).map(m -> ItemListResponse.builder()
                .id(m.getId())
                .name(m.getName())
                .createDate(m.getCreateDate().toString())
                .lastModifiedDate(m.getLastModifiedDate().toString())
                .build()
        );
        return itemLst;
    }

}
