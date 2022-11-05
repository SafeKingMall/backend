package com.safeking.shop.domain.item.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import com.safeking.shop.domain.admin.domain.entity.Admin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int quantity;

    private String description;

    private int price;


    //BaseEntity가 있는데 이게 꼭 필요한가?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

//    @OneToMany(mappedBy = "item", orphanRemoval = true, cascade = CascadeType.ALL)
//    private List<CategoryItem> categoryItems = new ArrayList<>();
//    public void addCategoryItem(CategoryItem categoryItem){
//        categoryItems.add(categoryItem);
//        categoryItem.addItem(this);
//    }

//    public static Item createItem(String name, int quantity, String description, Admin admin, List<CategoryItem>categoryItems){
//        Item item = new Item();
//        item.name=name;
//        item.quantity=quantity;
//        item.description=description;
//        item.admin=admin;
//
//        for (CategoryItem categoryItem : categoryItems) {
//            item.addCategoryItem(categoryItem);
//        }
//        return item;
//    }
    public static Item createItem(String name, int quantity, String description,int price, Admin admin){

        Item item = new Item();

        item.name=name;

        item.quantity=quantity;

        item.description=description;

        item.price=price;

        item.admin=admin;

        return item;
    }
    public void update(String name,int quantity,int price,String description){

        this.name=name;

        this.quantity=quantity;

        this.description=description;

        this.price=price;
    }
}
