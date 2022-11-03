package com.safeking.shop.domain.item.domain.service;

import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import com.safeking.shop.domain.item.domain.repository.ItemQuestionRepository;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionSaveDto;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion.ItemQuestionUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemQuestionService {

    private final ItemQuestionRepository itemQuestionRepository;

    private final ItemRepository itemRepository;

    public Long save(ItemQuestionSaveDto itemQuestionSaveDto){

        Item item = itemRepository.findById(itemQuestionSaveDto.getItemId()).orElseThrow();

        ItemQuestion itemQuestion = ItemQuestion
                .createItemQuestion(itemQuestionSaveDto.getTitle(), itemQuestionSaveDto.getContents(), item, itemQuestionSaveDto.getMember());

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
