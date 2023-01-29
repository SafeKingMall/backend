package com.safeking.shop.domain.item.domain.entity;

import com.safeking.shop.domain.common.BaseEntity;
import com.safeking.shop.domain.common.BaseTimeEntity;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
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
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "target",cascade = CascadeType.ALL,orphanRemoval = true)
    private ItemAnswer itemAnswer;

    private String password;

    public static ItemQuestion createItemQuestion(String title, String contents, Member writer, String password) {

        ItemQuestion itemQuestion = new ItemQuestion();

        itemQuestion.title = title;

        itemQuestion.contents = contents;

        itemQuestion.member = writer;

        itemQuestion.password = password;

        return itemQuestion;
    }

    public void addItemAnswer(ItemAnswer itemAnswer){
        this.itemAnswer=itemAnswer;
    }

    public void update(String title,String contents, String password){

        this.title=title;

        this.contents=contents;

        this.password=password;

    }

}
