package com.safeking.shop.domain.item.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemPhoto extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_photo_id")
    private Long id;

    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public static ItemPhoto create(String fileName, Item item){
        ItemPhoto itemPhoto = new ItemPhoto();
        itemPhoto.fileName = fileName;
        itemPhoto.item = item;
        return  itemPhoto;
    }

}
