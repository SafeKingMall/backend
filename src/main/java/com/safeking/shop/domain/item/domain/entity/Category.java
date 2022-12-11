package com.safeking.shop.domain.item.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

//    private int depth;

    private String name;

    private int sort;

    public static Category create(String name, int sort){

        Category category = new Category();
        category.name = name;
        category.sort = sort;
        return category;
    }
    public void update(String name, int sort) {

        this.name=name;
        this.sort=sort;
    }
}
