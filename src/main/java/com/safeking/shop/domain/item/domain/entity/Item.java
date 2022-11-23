package com.safeking.shop.domain.item.domain.entity;

import com.safeking.shop.domain.admin.common.BaseTimeEntity;
import com.safeking.shop.domain.admin.domain.entity.Admin;
import com.safeking.shop.domain.exception.ItemException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    */
    @Column(name = "admin_id")
    private String adminId;
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
    public static Item createItem(String name, int quantity, String description,int price, String adminId){

        Item item = new Item();

        item.name=name;

        item.quantity=quantity;

        item.description=description;

        item.price=price;

        item.adminId=adminId;

        return item;
    }
    public void update(String name,int quantity,int price, String description, String adminId){


        this.name=name;

        this.quantity=quantity;

        this.description=description;

        this.price=price;

        this.adminId=adminId;
    }

    /**
     * 상품수량 감소
     * @param count 주문 상품 수량
     */
    public void removeItemQuantity(int count) {
        int restStock = this.quantity - count;

        if(restStock < 0) {
            throw new ItemException("상품 재고가 부족합니다.");
        }
        this.quantity = restStock;
    }

    /**
     * 상품 재고 증가
     * @param count 주문상품취소 수량
     */
    public void addItemQuantity(int count) {
        this.quantity += count;
    }
}
