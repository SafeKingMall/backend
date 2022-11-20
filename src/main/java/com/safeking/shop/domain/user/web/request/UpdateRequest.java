package com.safeking.shop.domain.user.web.request;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.service.dto.MemberUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class UpdateRequest {
    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String name;
    @NotEmpty
//    @Pattern(regexp = "/^(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$/",message = "ex) 19971202")
    private String birth;
    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String representativeName;
    @NotNull
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phoneNumber;
    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String companyRegistrationNumber;
    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String corporateRegistrationNumber;
    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String zipcode;
    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String basicAddress;
    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String detailedAddress;

    public MemberUpdateDto toServiceDto(){
        return MemberUpdateDto.builder()
                .name(name)
                .birth(birth)
                .representativeName(representativeName)
                .phoneNumber(phoneNumber)
                .companyRegistrationNumber(companyRegistrationNumber)
                .corporateRegistrationNumber(corporateRegistrationNumber)
                .address(new Address(zipcode,basicAddress,detailedAddress))
                .build();
    }

}
