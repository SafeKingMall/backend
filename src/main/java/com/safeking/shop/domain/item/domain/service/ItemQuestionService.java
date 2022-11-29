package com.safeking.shop.domain.item.domain.service;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import com.safeking.shop.domain.item.domain.repository.ItemQuestionRepository;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionUpdateDto;
import com.safeking.shop.domain.order.web.OrderConst;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemQuestionService {

    private final ItemQuestionRepository itemQuestionRepository;

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    public Long save(ItemQuestionSaveDto itemQuestionSaveDto){

        Item item = itemRepository.findById(itemQuestionSaveDto.getItemId()).orElseThrow();

        Optional<Member> member = memberRepository.findByUsername(itemQuestionSaveDto.getMemberId());
        Member findMember = member.orElseThrow(() -> new OrderException(OrderConst.ORDER_MEMBER_NONE));
        ItemQuestion itemQuestion = ItemQuestion
                .createItemQuestion(itemQuestionSaveDto.getTitle(), itemQuestionSaveDto.getContents(), item, findMember);

        itemQuestionRepository.save(itemQuestion);

        return itemQuestion.getId();

    }

    public void update(ItemQuestionUpdateDto itemQuestionUpdateDto){

        ItemQuestion itemQuestion = itemQuestionRepository.findById(itemQuestionUpdateDto.getId()).orElseThrow();

        itemQuestion.update(itemQuestionUpdateDto.getTitle(),itemQuestionUpdateDto.getContents());

    }

    public void delete(Long id){

        ItemQuestion itemQuestion = itemQuestionRepository.findById(id).orElseThrow();

        itemQuestionRepository.delete(itemQuestion);

    }
}
