package com.safeking.shop.domain.item.web.query.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.safeking.shop.domain.item.domain.entity.QItemAnswer.itemAnswer;

import com.safeking.shop.domain.item.domain.entity.ItemAnswer;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer.ItemAnswerViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemAnswerQueryRepository {
    private final JPAQueryFactory queryFactory;


    public List<ItemAnswerViewDto> findAnswerByTargetQuestionId(Long id){
        List<ItemAnswer> list = queryFactory.selectFrom(itemAnswer).where(itemAnswer.target.id.eq(id)).fetch();
        List<ItemAnswerViewDto> viewList = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            ItemAnswer row = list.get(i);
            ItemAnswerViewDto dto = new ItemAnswerViewDto(
                    row.getId()
                    , row.getContents()
                    , row.getMember().getUsername()
                    , row.getCreateDate().toString()
                    , row.getLastModifiedDate().toString()
            );

            viewList.add(dto);
        }
        return viewList;
    }
}
