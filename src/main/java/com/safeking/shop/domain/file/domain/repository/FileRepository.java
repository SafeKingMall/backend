package com.safeking.shop.domain.file.domain.repository;

import com.safeking.shop.domain.file.domain.entity.File;
import com.safeking.shop.domain.item.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File,Long> {

}
