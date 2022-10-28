package com.safeking.shop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Long id;
    private String name;
    private int quantity;
    private String info;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categoryItemList=new ArrayList<>();
    @OneToMany(mappedBy = "item")
    private List<ItemPhoto> itemPhotos=new ArrayList<>();
    @OneToMany(mappedBy = "item")
    private List<ItemQNA> itemQNAS=new ArrayList<>();
    @OneToMany(mappedBy = "item")
    private List<CartItem> cartItems=new ArrayList<>();
    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems=new ArrayList<>();

}
