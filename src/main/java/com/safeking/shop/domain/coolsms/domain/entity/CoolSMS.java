package com.safeking.shop.domain.coolsms.domain.entity;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoolSMS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coolsms_id")
    private Long id;
    private String code;
    private String clientPhoneNumber;

    public CoolSMS(String code, String clientPhoneNumber) {
        this.code = code;
        this.clientPhoneNumber = clientPhoneNumber;
    }
}
