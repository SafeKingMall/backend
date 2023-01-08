package com.safeking.shop.domain.item.web.controller;

import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.service.ItemService;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemUpdateDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemViewDto;
import com.safeking.shop.domain.item.web.request.ItemRequest;
import com.safeking.shop.domain.item.web.request.ItemSaveRequest;
import com.safeking.shop.domain.item.web.request.ItemUpdateRequest;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static com.safeking.shop.global.jwt.TokenUtils.BEARER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class ItemController {

    private final ItemService itemService;

    @PostMapping("admin/item")
    public Long save(@RequestBody ItemSaveRequest itemSaveRequest, HttpServletRequest request){
        String username = TokenUtils.verify(request.getHeader(AUTH_HEADER).replace(BEARER, ""));
        itemSaveRequest.setAdminId(username);
        return itemService.save(itemSaveRequest);
    }

    @PutMapping("admin/item/{itemId}")
    public void update(@PathVariable Long itemId, @RequestBody ItemUpdateRequest itemUpdateRequest, HttpServletRequest request){
        String username = TokenUtils.verify(request.getHeader(AUTH_HEADER).replace(BEARER, ""));
        itemUpdateRequest.setAdminId(username);
        itemUpdateRequest.setId(itemId);
        itemService.update(itemUpdateRequest);
    }

    @DeleteMapping("admin/item/{itemId}")
    public void delete(@PathVariable Long itemId){

        itemService.delete(itemId);
    }

    @GetMapping("admin/item/{itemId}")
    public ItemViewResponse itemAdminView(@PathVariable Long itemId){
        ItemViewDto itemViewDto = itemService.view(itemId);
        ItemViewResponse itemViewResponse;
        itemViewResponse = new ItemViewResponse(itemViewDto.getId()
                , itemViewDto.getName()
                , itemViewDto.getQuantity()
                , itemViewDto.getDescription()
                , itemViewDto.getPrice()
                , itemViewDto.getAdminId()
                , itemViewDto.getCategoryName()
                , itemViewDto.getCreateDate()
                , itemViewDto.getLastModifiedDate()
                , itemViewDto.getFileName()
        );
        return itemViewResponse;
    }

    @GetMapping("admin/item/list")
    public Page<ItemListResponse> itemAdminList(@PageableDefault(size=10)Pageable pageable, @RequestParam(required = false, defaultValue = "") String itemName
        , @RequestParam(required = false, defaultValue = "") String categoryName
        ){
        Page<ItemListResponse> itemLst = itemService.List(pageable, itemName, categoryName);
        return itemLst;
    }

    @GetMapping("/item/{itemId}")
    public ItemViewResponse itemView(@PathVariable Long itemId){
        ItemViewResponse itemViewResponse;
        ItemViewDto itemViewDto = itemService.view(itemId);
        itemViewResponse = new ItemViewResponse(itemViewDto.getId()
                , itemViewDto.getName()
                , itemViewDto.getQuantity()
                , itemViewDto.getDescription()
                , ("Y".equals(itemViewDto.getViewYn())?itemViewDto.getPrice():null)
                , itemViewDto.getAdminId()
                , itemViewDto.getCategoryName()
                , itemViewDto.getCreateDate()
                , itemViewDto.getLastModifiedDate()
                , itemViewDto.getFileName()
        );
        return itemViewResponse;
    }

    @GetMapping("/item/list")
    public Page<ItemListResponse> itemList(@PageableDefault(size=10)Pageable pageable, @RequestParam(required = false, defaultValue = "") String itemName
            , @RequestParam(required = false, defaultValue = "") String categoryName
    ){
        Page<ItemListResponse> itemLst = itemService.List(pageable, itemName, categoryName);
        return itemLst;
    }

    @PostMapping("admin/itemPhoto/{itemId}")
    public void savePhoto(@RequestParam(name = "file") MultipartFile file, @PathVariable Long itemId) throws IOException {
        itemService.photoUpload(file, itemId);
    }
}
