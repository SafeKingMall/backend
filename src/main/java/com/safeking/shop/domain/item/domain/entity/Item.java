package com.safeking.shop.domain.item.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import com.safeking.shop.domain.admin.domain.entity.Admin;
import com.safeking.shop.domain.exception.ItemException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.annotation.Nullable;
import javax.persistence.*;

@Entity @Getter@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Item extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int quantity;

    private String description;

    private int price;

    @Column(name="view_yn")
    @ColumnDefault("'Y'")
    @Nullable
    private String viewYn = "Y";


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "admin_id")
    private String adminId;

    @Nullable
    private int viewPrice;

    public static Item createItem(String name, int quantity, String description,int price, String adminId, Category category, int viewPrice, String viewYn){

        Item item = new Item();

        item.name=name;

        item.quantity=quantity;

        item.description=description;

        item.price=price;

        item.adminId=adminId;

        item.category=category;

        item.viewPrice=viewPrice;

        item.viewYn=viewYn;

        return item;
    }
    public void update(String name,int quantity,int price, String description, String adminId, Category category, String viewYn, int viewPrice){


        this.name=name;

        this.quantity=quantity;

        this.description=description;

        this.price=price;

        this.adminId=adminId;

        this.category=category;

        this.viewYn=viewYn;

        this.viewPrice=viewPrice;
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
