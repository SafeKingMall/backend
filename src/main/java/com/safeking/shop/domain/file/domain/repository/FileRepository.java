package com.safeking.shop.domain.file.domain.repository;

import com.safeking.shop.domain.file.domain.entity.File;
import com.safeking.shop.domain.file.web.response.FileListResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File,Long> {

    List<FileListResponse> findAllByTargetId(Long targetId);

    List<FileListResponse> findAllByTargetIdAndType(Long targetId, String type);
}
