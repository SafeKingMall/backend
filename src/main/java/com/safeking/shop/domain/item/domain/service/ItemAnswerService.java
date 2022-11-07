package com.safeking.shop.domain.item.domain.service;

import com.safeking.shop.domain.item.domain.entity.ItemAnswer;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import com.safeking.shop.domain.item.domain.repository.ItemAnswerRepository;
import com.safeking.shop.domain.item.domain.repository.ItemQuestionRepository;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer.ItemAnswerSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer.ItemAnswerUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemAnswerService {

    private final ItemAnswerRepository itemAnswerRepository;

    private final ItemQuestionRepository itemQuestionRepository;

    public Long save(ItemAnswerSaveDto itemAnswerSaveDto){

        ItemQuestion itemQuestion = itemQuestionRepository.findById(itemAnswerSaveDto.getItemQuestionId()).orElseThrow();

        ItemAnswer itemAnswer = ItemAnswer.createItemAnswer(itemAnswerSaveDto.getAdmin(), itemQuestion, itemAnswerSaveDto.getContents());

        itemQuestionRepository.save(itemQuestion);

        return itemAnswer.getId();
    }
    public void update(ItemAnswerUpdateDto itemAnswerUpdateDto){

        ItemAnswer itemAnswer = itemAnswerRepository.findById(itemAnswerUpdateDto.getId()).orElseThrow();

        itemAnswer.update(itemAnswerUpdateDto.getContents());

    }
    public void delete(Long id){

        ItemAnswer itemAnswer = itemAnswerRepository.findById(id).orElseThrow();

        itemAnswerRepository.delete(itemAnswer);


    }
}
