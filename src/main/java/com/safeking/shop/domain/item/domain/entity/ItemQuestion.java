package com.safeking.shop.domain.item.domain.entity;

import com.safeking.shop.domain.admin.common.BaseTimeEntity;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemQuestion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_question_id")
    private Long id;
    private String title;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @OneToOne(mappedBy = "target",cascade = CascadeType.ALL,orphanRemoval = true)
    private ItemAnswer itemAnswer;

    public static ItemQuestion createItemQuestion(String title, String contents, Item item, Member writer) {

        ItemQuestion itemQuestion = new ItemQuestion();

        itemQuestion.title = title;

        itemQuestion.contents = contents;

        itemQuestion.item = item;

        itemQuestion.writer = writer;

        return itemQuestion;
    }

    public void addItemAnswer(ItemAnswer itemAnswer){
        this.itemAnswer=itemAnswer;
    }

    public void update(String title,String contents){

        this.title=title;

        this.contents=contents;

    }

}
