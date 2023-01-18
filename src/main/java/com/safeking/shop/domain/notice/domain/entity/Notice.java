package com.safeking.shop.domain.notice.domain.entity;

import com.safeking.shop.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", nullable = false)
    private Long id;

    private String title;

    private String contents;

    @Column(name="member_id")
    private String memberId;

    public static Notice create(String title, String contents, String memberId){
        Notice notice = new Notice();
        notice.title = title;
        notice.contents = contents;
        notice.memberId = memberId;
        return notice;
    }

    public void update(String title, String contents, String memberId){
        this.title = title;
        this.contents = contents;
        this.memberId = memberId;
    }

}
