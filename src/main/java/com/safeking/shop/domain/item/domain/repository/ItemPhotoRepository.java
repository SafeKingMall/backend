package com.safeking.shop.domain.item.domain.repository;

import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.entity.ItemPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPhotoRepository extends JpaRepository<ItemPhoto,Long> {

    ItemPhoto findTop1ByItemIdOrderByCreateDateDesc(Long id);

    List<ItemPhoto> findByItemId(Long id);
}
