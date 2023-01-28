package com.safeking.shop.domain.file.domain.service;

import com.safeking.shop.domain.file.domain.entity.File;
import com.safeking.shop.domain.file.domain.repository.FileRepository;
import com.safeking.shop.domain.file.domain.service.servicedto.FileSaveDto;
import com.safeking.shop.domain.file.web.response.FileListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FileService {

    private final FileRepository fileRepository;

    @Value("${spring.upload.path}")
    private String uploadPath;

    public Long save(MultipartFile file, String type, Long targetId) throws IOException {
        String orgFileName = file.getOriginalFilename();
        int pos = orgFileName.lastIndexOf(".");
        String ext = orgFileName.substring(pos+1);
        String realName = UUID.randomUUID().toString() + "." + ext;

        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        String nowDt = format.format(now);

        String urlPath = "/file/"+ type + "/" + targetId.toString() +"/"+ nowDt +"/" ;
        String path = uploadPath + urlPath +realName;
        java.io.File folder = new java.io.File(path);
        if(!folder.exists()){
            folder.mkdirs();
        }

        String fullPath = path;
        file.transferTo(new java.io.File(fullPath));

        File file1 = File.create(targetId
                , type
                , urlPath
                , realName
                , orgFileName
        );
        return fileRepository.save(file1).getId();
    }

    public List<FileListResponse> list(String type, Long targetId){
        return fileRepository.findAllByTargetIdAndType(targetId, type);
    }

    public void delete(Long id) throws Exception {
        File file = fileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("파일이 없습니다."));

        java.io.File sfile = new java.io.File(uploadPath+file.getFilePath()+file.getFileName());
        boolean result = sfile.delete();
        /*
        if(!result){
            throw new Exception("파일삭제에 실패했습니다.");
        }
         */

        fileRepository.delete(file);
    }
}
