package com.safeking.shop.domain.item.domain.service;

import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.CategoryRepository;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemListDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemViewDto;
import com.safeking.shop.domain.item.web.request.ItemSaveRequest;
import com.safeking.shop.domain.item.web.request.ItemUpdateRequest;
import com.safeking.shop.domain.item.web.response.ItemAdminListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ItemService {

    private final CategoryRepository categoryRepository;

    private final ItemRepository itemRepository;


    @Value("${spring.upload.path}")
    private String uploadPath;

    public Long save(ItemSaveRequest itemSaveRequest){

        Category category = categoryRepository.findById(itemSaveRequest.getCategoryId()).orElseThrow();
        Item item = Item.createItem(itemSaveRequest.getName()
                , itemSaveRequest.getQuantity()
                , itemSaveRequest.getDescription()
                , itemSaveRequest.getPrice()
                , itemSaveRequest.getAdminId()
                , category
                , ("N".equals(itemSaveRequest.getViewYn())?1000000000:itemSaveRequest.getPrice())
                , itemSaveRequest.getViewYn()
        );

        itemRepository.save(item);

        return item.getId();
    }

    public void update(ItemUpdateRequest itemUpdateRequest){
        //기존에 item이 있다고 가정
        Item item = itemRepository.findById(itemUpdateRequest.getId()).orElseThrow();
        Category category = categoryRepository.findById(itemUpdateRequest.getCategoryId()).orElseThrow();
        item.update(itemUpdateRequest.getName()
                , itemUpdateRequest.getQuantity()
                , itemUpdateRequest.getPrice()
                , itemUpdateRequest.getDescription()
                , itemUpdateRequest.getAdminId()
                , category
                , itemUpdateRequest.getViewYn()
                , ("N".equals(itemUpdateRequest.getViewYn())?1000000000:itemUpdateRequest.getPrice())
        );

    }
    public void delete(Long id){

        Item item = itemRepository.findById(id).orElseThrow();
        java.io.File sfile = new java.io.File(uploadPath+item.getFileName());
        boolean result = sfile.delete();
        itemRepository.delete(item);

    }

    public ItemViewDto view(Long id){
        Item item = itemRepository.findById(id).orElseThrow();
        log.info("Item.viewYn : "+ item.getViewYn());
        ItemViewDto itemViewDto = new ItemViewDto(item.getId(),
                item.getName(),
                item.getQuantity(),
                item.getDescription(),
                item.getViewPrice(),
                item.getPrice(),
                item.getAdminId(),
                (item.getCategory()==null?null:item.getCategory().getName()),
                item.getCreateDate().toString(),
                item.getLastModifiedDate().toString(),
                item.getViewYn(),
                (item.getFileName())
                );

        return itemViewDto;
    }

    public Page<ItemListDto> List(Pageable pageable, String itemName, String categoryName){
        Page<ItemListDto> posts = null;
        Page<Item> pItem = null;
        if(!"".equals(itemName)){

            pItem = itemRepository.findByNameContaining(pageable, itemName);
        }else if(!"".equals(categoryName)){

            pItem = itemRepository.findByCategoryNameContaining(pageable, categoryName);
        }else{

            pItem = itemRepository.findAll(pageable);
        }
        posts = pItem.map(m-> ItemListDto.builder()
                .id(m.getId())
                .viewPrice(m.getViewPrice())
                .price(m.getPrice())
                .name(m.getName())
                .categoryName((m.getCategory()==null?null:m.getCategory().getName()))
                .createDate(m.getCreateDate().toString())
                .lastModifiedDate(m.getLastModifiedDate().toString())
                .fileName(m.getFileName())
                .quantity(m.getQuantity())
                .build());
        return posts;
    }

    public Page<ItemAdminListResponse> adminList(Pageable pageable, String itemName, String categoryName){
        Page<ItemAdminListResponse> posts = null;
        Page<Item> pItem = null;
        if(!"".equals(itemName)){

            pItem = itemRepository.findByNameContaining(pageable, itemName);
        }else if(!"".equals(categoryName)){

            pItem = itemRepository.findByCategoryNameContaining(pageable, categoryName);
        }else{

            pItem = itemRepository.findAll(pageable);
        }
        posts = pItem.map(m-> ItemAdminListResponse.builder()
                .id(m.getId())
                .price(m.getPrice())
                .name(m.getName())
                .categoryName((m.getCategory()==null?null:m.getCategory().getName()))
                .createDate(m.getCreateDate().toString())
                .lastModifiedDate(m.getLastModifiedDate().toString())
                .fileName(m.getFileName())
                .quantity(m.getQuantity())
                .build());

        return posts;
    }

    public void photoUpload(MultipartFile file, Long itemId) throws IOException {
        String orgFileName = file.getOriginalFilename();
        int pos = orgFileName.lastIndexOf(".");
        String ext = orgFileName.substring(pos+1);
        String realName = UUID.randomUUID().toString() + "." + ext;

        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        String nowDt = format.format(now);

        String urlPath = "/file/item/"+ itemId.toString() +"/"+ nowDt +"/" +realName;
        String path = uploadPath + urlPath;
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdirs();
        }

        String fullPath = path;
        file.transferTo(new java.io.File(fullPath));

        Item item = itemRepository.findById(itemId).orElseThrow();
        item.updateFile(urlPath);

    }

    public void photoDelete(Long id){
        Item item = itemRepository.findById(id).orElseThrow();
        java.io.File sfile = new java.io.File(uploadPath+item.getFileName());
        boolean result = sfile.delete();
        item.updateFile(null);

    }
}
