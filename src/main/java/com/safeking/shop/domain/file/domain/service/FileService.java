package com.safeking.shop.domain.file.domain.service;

import com.safeking.shop.domain.file.domain.entity.File;
import com.safeking.shop.domain.file.domain.repository.FileRepository;
import com.safeking.shop.domain.file.domain.service.servicedto.FileSaveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FileService {

    private final FileRepository fileRepository;

    public Long save(MultipartFile file, String type, Long targetId) throws IOException {
        String orgFileName = file.getOriginalFilename();
        int pos = orgFileName.lastIndexOf(".");
        String ext = orgFileName.substring(pos+1);
        String realName = UUID.randomUUID().toString() + "." + ext;

        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));

        String fullPath = "c:/test/"+realName;
        file.transferTo(new java.io.File(fullPath));

        File file1 = File.create(targetId
                , type
                , fullPath
                , realName
                , orgFileName
        );
        return fileRepository.save(file1).getId();
    }
}
