package com.safeking.shop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class ItemPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemphoto_id", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

}
