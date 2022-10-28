package com.safeking.shop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter@Setter
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id", nullable = false)
    private Long id;
    private String loginId;
    private String password;
    @OneToMany(mappedBy = "admin")
    private List<Item>items=new ArrayList<>();
    @OneToMany(mappedBy = "admin")
    private List<ItemQNAReply>itemQNAReplies=new ArrayList<>();

}
