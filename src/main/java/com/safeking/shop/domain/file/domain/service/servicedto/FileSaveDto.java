package com.safeking.shop.domain.file.domain.service.servicedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileSaveDto {

    private Long targetId;

    private String type;

    private String filePath;

    private String fileName;

    private String orgFileName;

}
