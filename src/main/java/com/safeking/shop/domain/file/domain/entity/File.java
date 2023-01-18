package com.safeking.shop.domain.file.domain.entity;

import com.safeking.shop.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id", nullable = false)
    private Long id;

    @Column(name = "target_id")
    private Long targetId;

    private String type;

    private String filePath;

    private String fileName;

    private String orgFileName;

    public static File create(Long targetId, String type, String filePath, String fileName, String orgFileName){
        File file = new File();
        file.targetId = targetId;
        file.type = type;
        file.filePath = filePath;
        file.fileName = fileName;
        file.orgFileName = orgFileName;
        return file;
    }
}
