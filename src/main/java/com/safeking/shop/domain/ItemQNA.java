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
public class ItemQNA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemqna_id", nullable = false)
    private Long id;
    private String title;
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "itemQNA")
    private List<ItemQNAReply>itemQNAReplies=new ArrayList<>();
}
