package com.safeking.shop.global.annotation;

import com.safeking.shop.global.RestDocsConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//런타임시
@Target(ElementType.TYPE)//클래스에 선언
@ActiveProfiles("test")//test 코드와 분리
@SpringBootTest//spring 기반의 test
@AutoConfigureMockMvc//mockMvc 를 자동으로 설정
@AutoConfigureRestDocs// resdocs 를 자동으로 설정
@Import(RestDocsConfiguration.class)
@Transactional
public @interface ApiTest {
}
