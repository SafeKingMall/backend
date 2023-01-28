package com.safeking.shop.domain.payment.convert;

import com.safeking.shop.domain.payment.domain.entity.CustomCardCodeConstant;
import com.safeking.shop.domain.payment.domain.repository.CustomCardCodeRepository;
import com.siot.IamportRestClient.constant.CardConstant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
public class ConvertTest {

    @Test
    void test() {
        String element = CustomCardCodeRepository.getElement(CardConstant.CODE_BC);
        Assertions.assertThat(element).isEqualTo(CustomCardCodeConstant.KR_CODE_BC);
    }
}
