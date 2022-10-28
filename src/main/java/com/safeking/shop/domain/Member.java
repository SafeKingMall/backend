package com.safeking.shop.domain;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;
    private String userId;
    private String password;
    private String username;
    private String email;
    private LocalDateTime localDateTime;
    private String phone;
    @OneToOne(mappedBy = "member")
    private Cart cart;
    @OneToMany(mappedBy = "member")
    private List<ItemQNA> itemQNAList=new ArrayList<>();
    @OneToOne(mappedBy = "member")
    private SocialAccount socialAccount;
    @OneToMany(mappedBy = "member")
    private List<Board>boards=new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Order>orders=new ArrayList<>();
}
