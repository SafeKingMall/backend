package com.safeking.shop.domain.user.web.request.signuprequest;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.service.dto.MemberInfoDto;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class MemberInfo {
    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String companyName;

    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String companyRegistrationNumber;

    @Length(max = 50)
    private String corporateRegistrationNumber;

    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String representativeName;

    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String zipcode;

    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String basicAddress;

    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String detailedAddress;

    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String contact;

    public MemberInfoDto toServiceDto(){
        return MemberInfoDto.builder()
                .companyName(companyName)
                .companyRegistrationNumber(companyRegistrationNumber)
                .corporateRegistrationNumber(corporateRegistrationNumber)
                .representativeName(representativeName)
                .address(new Address(basicAddress, detailedAddress, zipcode))
                .contact(contact)
                .build();
    }
}
