package com.safeking.shop.domain.user.domain.entity;

import com.safeking.shop.domain.admin.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoDetails extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_info_details")
    private Long id;

    // TODO 본인인증 정보 추가
}
