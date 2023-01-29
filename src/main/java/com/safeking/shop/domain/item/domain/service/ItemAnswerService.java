package com.safeking.shop.domain.item.domain.service;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.entity.ItemAnswer;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import com.safeking.shop.domain.item.domain.repository.ItemAnswerRepository;
import com.safeking.shop.domain.item.domain.repository.ItemQuestionRepository;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer.ItemAnswerListDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer.ItemAnswerSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer.ItemAnswerUpdateDto;
import com.safeking.shop.domain.item.web.request.ItemAnswerSaveRequest;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemAnswerService {

    private final ItemAnswerRepository itemAnswerRepository;

    private final ItemQuestionRepository itemQuestionRepository;

    private final MemberRepository memberRepository;

    public Long save(ItemAnswerSaveRequest itemAnswerSaveRequest) throws Exception {

        ItemQuestion itemQuestion = itemQuestionRepository.findById(itemAnswerSaveRequest.getItemQnaId()).orElseThrow();

        Optional<Member> member = memberRepository.findByUsername(itemAnswerSaveRequest.getMemberId());
        Member findMember = member.orElseThrow(() -> new Exception("회원정보가 없습니다."));

        ItemAnswer itemAnswer = ItemAnswer.createItemAnswer(findMember, itemQuestion, itemAnswerSaveRequest.getContents());

        itemQuestionRepository.save(itemQuestion);

        return itemAnswer.getId();
    }
    public void update(ItemAnswerSaveRequest itemAnswerSaveRequest){

        ItemAnswer itemAnswer = itemAnswerRepository.findById(itemAnswerSaveRequest.getId()).orElseThrow();

        itemAnswer.update(itemAnswerSaveRequest.getContents());

    }
    public void delete(Long id){

        ItemAnswer itemAnswer = itemAnswerRepository.findById(id).orElseThrow();

        itemAnswerRepository.delete(itemAnswer);


    }

    public Page<ItemAnswerListDto> list(Long itemQnaId, Pageable pageable){
        Page<ItemAnswer> list = itemAnswerRepository.findByTargetId(pageable, itemQnaId);
        Page<ItemAnswerListDto> itemAnswerListDto = list.map(m->ItemAnswerListDto.builder()
                .id(m.getId())
                .memberId(m.getMember().getUsername())
                .contents(m.getContents())
                .createDate(m.getCreateDate().toString())
                .lastModifiedDate(m.getLastModifiedDate().toString())
                .itemQnaId(m.getTarget().getId())
                .build()
        );
        return itemAnswerListDto;
    }
}
