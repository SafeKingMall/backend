package com.safeking.shop.domain.item.domain.repository;

import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.entity.ItemAnswer;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class ItemAnswerRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemQuestionRepository questionRepository;
    @Autowired
    ItemAnswerRepository answerRepository;
    @Test
    void deleteByTarget() {
        //given
        GeneralMember member = GeneralMember.builder().build();
        GeneralMember savedMember = memberRepository.save(member);

        Item savedItem = itemRepository.save(new Item());

        ItemQuestion itemQuestion = ItemQuestion.createItemQuestion(
                "title"
                , "contents"
                , savedItem
                , savedMember);
        questionRepository.save(itemQuestion);

        ItemAnswer itemAnswer = ItemAnswer.createItemAnswer(savedMember
                , itemQuestion
                , "contents");
        ItemAnswer savedItemAnswer = answerRepository.save(itemAnswer);
        //when
        answerRepository.deleteByTarget(itemQuestion);
        //then
        Assertions.assertThrows(NoSuchElementException.class
                ,() -> answerRepository
                        .findById(savedItemAnswer.getId())
                        .orElseThrow());

    }
}