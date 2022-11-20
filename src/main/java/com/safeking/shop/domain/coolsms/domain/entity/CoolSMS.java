package com.safeking.shop.domain.coolsms.domain.entity;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoolSMS {

    private Long id;
    private String code;
    private String clientPhoneNumber;

    private LocalDateTime createTime;

    private LocalDateTime endTime;


    public CoolSMS(String code, String clientPhoneNumber) {
        this.code = code;
        this.clientPhoneNumber = clientPhoneNumber;
        this.createTime=LocalDateTime.now();
        this.endTime=LocalDateTime.now().plusMinutes(3);
    }

    public boolean isExpired(){
        return this.endTime.isBefore(LocalDateTime.now());
    }

    public void changeId(Long id) {
        this.id = id;
    }

}
