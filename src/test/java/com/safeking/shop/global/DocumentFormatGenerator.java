package com.safeking.shop.global;

import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.key;

public interface DocumentFormatGenerator {

    static Attributes.Attribute IdValidation() {
        return key("format").value("아이디는 숫자', '문자' 무조건 1개 이상, '최소 8자에서 최대 20자");
    }
    static Attributes.Attribute PWValidation() {
        return key("format").value("비밀번호는 특수문자, 영문, 숫자 조합 (8~10 자리)이어야 합니다.");
    }
    static Attributes.Attribute EmailValidation() {
        return key("format").value("이메일 형식이어야 합니다.");
    }
    static Attributes.Attribute PhoneNumberValidation() {
        return key("format").value("10 ~ 11 자리의 숫자만 입력 가능합니다.");
    }
    static Attributes.Attribute BirthValidation() {
        return key("format").value("8자리의 생년월일, ex)19971202");
    }
    static Attributes.Attribute InputValidation() {
        return key("format").value("빈 문자여서는 안되며 50글자이내여야 합니다.");
    }
    static Attributes.Attribute BooleanValidation() {
        return key("format").value("null이어서는 안되며 boolean형이어야 합니다.");
    }
    static Attributes.Attribute companyRegistrationNumberValidation() {return key("format").value("ex) 123-12-12345");}
    static Attributes.Attribute JwtTokenValidation() {return key("format").value("ex) Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");}
    static Attributes.Attribute RefreshTokenValidation() {return key("format").value("ex) Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");}
    static Attributes.Attribute CountValidation() {return key("format").value("Max size: 50000");}
    static Attributes.Attribute ItemIdValidation() {return key("format").value("ex) /api/v1/user/cartItem?itemId= 2 &itemId= 4 &itemId= 6");}

}
