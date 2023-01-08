package com.safeking.shop.domain.item.domain.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import com.safeking.shop.domain.item.domain.repository.ItemAnswerRepository;
import com.safeking.shop.domain.item.domain.repository.ItemQuestionRepository;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionListDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionUpdateDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionViewDto;
import com.safeking.shop.domain.item.web.query.repository.ItemAnswerQueryRepository;
import com.safeking.shop.domain.order.constant.OrderConst;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final ItemAnswerRepository itemAnswerRepository;

    private final JPAQueryFactory queryFactory;

    private final ItemAnswerQueryRepository itemAnswerQueryRepository;

    public Long save(ItemQuestionSaveDto itemQuestionSaveDto){

        Item item = itemRepository.findById(itemQuestionSaveDto.getItemId()).orElseThrow();

        Optional<Member> member = memberRepository.findByUsername(itemQuestionSaveDto.getMemberId());
        Member findMember = member.orElseThrow(() -> new OrderException(OrderConst.ORDER_MEMBER_NONE));
        ItemQuestion itemQuestion = ItemQuestion
                .createItemQuestion(itemQuestionSaveDto.getTitle(), itemQuestionSaveDto.getContents(), item, findMember);

        itemQuestionRepository.save(itemQuestion);

        return itemQuestion.getId();

    }

    public void update(ItemQuestionUpdateDto itemQuestionUpdateDto,String username){

        ItemQuestion itemQuestion = itemQuestionRepository.findById(itemQuestionUpdateDto.getId()).orElseThrow();
        //임시로 만듦
        if(!itemQuestion.getWriter().getUsername().equals(username)) throw new IllegalArgumentException("권한이 없습니다.");

        itemQuestion.update(itemQuestionUpdateDto.getTitle(),itemQuestionUpdateDto.getContents());
    }

    public void delete(Long id, String username){

        ItemQuestion itemQuestion = itemQuestionRepository.findById(id).orElseThrow();
        //임시로 만듦
        if(!itemQuestion.getWriter().getUsername().equals(username)) throw new IllegalArgumentException("권한이 없습니다.");

        itemQuestionRepository.delete(itemQuestion);

    }

    public ItemQuestionViewDto view(Long id){
        ItemQuestion itemQuestion = itemQuestionRepository.findById(id).orElseThrow();
        ItemQuestionViewDto itemQuestionViewDto = new ItemQuestionViewDto(
                itemQuestion.getId()
                , itemQuestion.getTitle()
                , itemQuestion.getContents()
                , itemQuestion.getItem().getId()
                , itemQuestion.getWriter().getUsername()
                , itemAnswerQueryRepository.findAnswerByTargetQuestionId(itemQuestion.getId())
        );
        return itemQuestionViewDto;
    }

    public Page<ItemQuestionListDto> list(Pageable pageable){
        Page<ItemQuestionListDto> posts = itemQuestionRepository.findAll(pageable).map(m->ItemQuestionListDto.builder()
                .id(m.getId())
                .title(m.getTitle())
                .itemId(m.getItem().getId())
                .memberId(m.getWriter().getUsername())
                .createDate(m.getCreateDate().toString())
                .lastModifiedDate(m.getLastModifiedDate().toString())
                .build()
        );
        return posts;
    }
}
