package com.safeking.shop.domain.file.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FileListResponse {
    private Long id;

    private String fileName;

    private String filePath;

    private String orgFileName;

    private String type;

    private Long targetId;
}
