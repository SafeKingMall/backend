package com.safeking.shop.domain.file.web.controller;

import com.safeking.shop.domain.file.domain.service.FileService;
import com.safeking.shop.domain.file.domain.service.servicedto.FileSaveDto;
import com.safeking.shop.domain.item.domain.service.ItemService;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemUpdateDto;
import com.safeking.shop.domain.item.domain.service.servicedto.item.ItemViewDto;
import com.safeking.shop.domain.item.web.response.ItemListResponse;
import com.safeking.shop.domain.item.web.response.ItemViewResponse;
import com.safeking.shop.global.jwt.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static com.safeking.shop.global.jwt.TokenUtils.BEARER;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/file/upload")
    public Long save(@RequestParam(name = "type") String type,
                     @RequestParam(name = "targetId") Long targetId,
                     @RequestParam(name = "file") MultipartFile file) throws IOException {

        return fileService.save(file, type, targetId);
    }
}
