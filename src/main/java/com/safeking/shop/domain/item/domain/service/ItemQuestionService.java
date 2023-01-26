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
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemQuestionService {

    private final ItemQuestionRepository itemQuestionRepository;

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final ItemAnswerQueryRepository itemAnswerQueryRepository;

    private final CustomBCryPasswordEncoder encoder;

    public Long save(ItemQuestionSaveDto itemQuestionSaveDto){

        Item item = itemRepository.findById(itemQuestionSaveDto.getItemId()).orElseThrow();

        Optional<Member> member = memberRepository.findByUsername(itemQuestionSaveDto.getMemberId());
        Member findMember = member.orElseThrow(() -> new OrderException(OrderConst.ORDER_MEMBER_NONE));
        ItemQuestion itemQuestion = ItemQuestion
                .createItemQuestion(itemQuestionSaveDto.getTitle(), itemQuestionSaveDto.getContents(), item, findMember, encoder.encode(itemQuestionSaveDto.getPassword()));

        itemQuestionRepository.save(itemQuestion);

        return itemQuestion.getId();

    }

    public void update(ItemQuestionUpdateDto itemQuestionUpdateDto,String username){

        ItemQuestion itemQuestion = itemQuestionRepository.findById(itemQuestionUpdateDto.getId()).orElseThrow();
        //임시로 만듦
        if(!itemQuestion.getMember().getUsername().equals(username)) throw new IllegalArgumentException("권한이 없습니다.");

        itemQuestion.update(itemQuestionUpdateDto.getTitle(),itemQuestionUpdateDto.getContents(), encoder.encode(itemQuestionUpdateDto.getPassword()));
    }

    public void delete(Long id, String username){

        ItemQuestion itemQuestion = itemQuestionRepository.findById(id).orElseThrow();
        //임시로 만듦
        if(!itemQuestion.getMember().getUsername().equals(username)) throw new IllegalArgumentException("권한이 없습니다.");

        itemQuestionRepository.delete(itemQuestion);

    }

    public ItemQuestionViewDto view(Long id){
        ItemQuestion itemQuestion = itemQuestionRepository.findById(id).orElseThrow();
        ItemQuestionViewDto itemQuestionViewDto = new ItemQuestionViewDto(
                itemQuestion.getId()
                , itemQuestion.getTitle()
                , itemQuestion.getContents()
                , itemQuestion.getItem().getId()
                , itemQuestion.getMember().getUsername()
                , itemAnswerQueryRepository.findAnswerByTargetQuestionId(itemQuestion.getId())
                , itemQuestion.getPassword()
        );
        return itemQuestionViewDto;
    }

    public Page<ItemQuestionListDto> listAndTitle(Pageable pageable, String title){
        Page<ItemQuestionListDto> posts = itemQuestionRepository.findByTitleContaining(pageable, title).map(m->ItemQuestionListDto.builder()
                .id(m.getId())
                .title(m.getTitle())
                .itemId(m.getItem().getId())
                .memberId(m.getMember().getUsername())
                .createDate(m.getCreateDate().toString())
                .lastModifiedDate(m.getLastModifiedDate().toString())
                .build()
        );
        return posts;
    }

    public Page<ItemQuestionListDto> listAndMemeberId(Pageable pageable, String memberId){
        Page<ItemQuestionListDto> posts = itemQuestionRepository.findByMemberUsername(pageable, memberId).map(m->ItemQuestionListDto.builder()
                .id(m.getId())
                .title(m.getTitle())
                .itemId(m.getItem().getId())
                .memberId(m.getMember().getUsername())
                .createDate(m.getCreateDate().toString())
                .lastModifiedDate(m.getLastModifiedDate().toString())
                .build()
        );
        return posts;
    }

    public Page<ItemQuestionListDto> listAndCreateDate(Pageable pageable, LocalDateTime startDateTime, LocalDateTime endDateTime){
        Page<ItemQuestionListDto> posts = itemQuestionRepository.findByCreateDateBetween(pageable, startDateTime, endDateTime).map(m->ItemQuestionListDto.builder()
                .id(m.getId())
                .title(m.getTitle())

                .itemId(m.getItem().getId())
                .memberId(m.getMember().getUsername())
                .createDate(m.getCreateDate().toString())
                .lastModifiedDate(m.getLastModifiedDate().toString())
                .build()
        );
        return posts;
    }
    public Page<ItemQuestionListDto> list(Pageable pageable){
        Page<ItemQuestionListDto> posts = itemQuestionRepository.findAll(pageable).map(m->ItemQuestionListDto.builder()
                .id(m.getId())
                .title(m.getTitle())
                .itemId(m.getItem().getId())
                .memberId(m.getMember().getUsername())
                .createDate(m.getCreateDate().toString())
                .lastModifiedDate(m.getLastModifiedDate().toString())
                .build()
        );
        return posts;
    }

    public ItemQuestionViewDto viewPassword(Long id, String password) throws Exception {
        ItemQuestion itemQuestion = itemQuestionRepository.findById(id).orElseThrow();
        System.out.println(encoder.matches(password, itemQuestion.getPassword()) );
        if(encoder.matches(password, itemQuestion.getPassword()) == false){
            throw new Exception("비밀번호가 틀립니다.");
        }
        ItemQuestionViewDto itemQuestionViewDto = new ItemQuestionViewDto(
                itemQuestion.getId()
                , itemQuestion.getTitle()
                , itemQuestion.getContents()
                , itemQuestion.getItem().getId()
                , itemQuestion.getMember().getUsername()
                , itemAnswerQueryRepository.findAnswerByTargetQuestionId(itemQuestion.getId())
                , itemQuestion.getPassword()
        );
        return itemQuestionViewDto;
    }
}
