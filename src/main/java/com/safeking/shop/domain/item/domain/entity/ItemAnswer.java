package com.safeking.shop.domain.item.domain.entity;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import com.safeking.shop.domain.admin.common.BaseTimeEntity;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemAnswer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_answer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_question_id")
    private ItemQuestion target;

    private String contents;

    public static ItemAnswer createItemAnswer(Member member, ItemQuestion target, String contents) {
        ItemAnswer itemAnswer = new ItemAnswer();

        itemAnswer.member = member;

        itemAnswer.contents = contents;

        itemAnswer.target = target;

        target.addItemAnswer(itemAnswer);

        return itemAnswer;
    }

    public void update(String contents){
        this.contents=contents;
    }

}
