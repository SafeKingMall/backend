package com.safeking.shop.domain.payment.convert;

import com.safeking.shop.domain.payment.domain.entity.CustomCardCodeConstant;
import com.safeking.shop.domain.payment.domain.repository.CustomPaymentRepository;
import com.siot.IamportRestClient.constant.CardConstant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@SpringBootTest
public class ConvertTest {

    @Test
    void test() {
        String element = CustomPaymentRepository.getCardCodeElement(CardConstant.CODE_BC);
        Assertions.assertThat(element).isEqualTo(CustomCardCodeConstant.KR_CODE_BC);
    }
    @Test
    void test2() {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
        System.out.println(localDateTime);
    }
}
