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

}