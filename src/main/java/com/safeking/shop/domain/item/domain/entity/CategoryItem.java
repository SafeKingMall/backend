package com.safeking.shop.domain.item.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryItem extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="item_id" )
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="category_id" )
    private Category category;


    public CategoryItem(Category category,Item item) {
        this.category = category;
        this.item=item;
    }

    public void update(Category category){
        this.category=category;
    }

    public void addItem(Item item){
        this.item=item;
    }

}
