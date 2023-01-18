package com.safeking.shop.domain.user.web.request;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.service.dto.MemberUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@Builder
public class UpdateRequest {
    @Length(max = 50)
    @NotBlank(message = "빈 문자는 안됩니다.")
    private String name;
    @NotBlank
    @Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$",message = "ex) 19971202")
    private String birth;
    @Email
    private String email;
    @Length(max = 50)
    @NotBlank(message = "빈 문자는 안됩니다.")
    private String representativeName;
    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phoneNumber;
    @Pattern(regexp = "([0-9]{3})-?([0-9]{2})-?([0-9]{5})",message = "ex)123-12-12345")
    @NotBlank(message = "빈 문자는 안됩니다.")
    private String companyRegistrationNumber;
    @Length(max = 50)
    private String corporateRegistrationNumber;

    @Length(max = 50)
    @NotBlank(message = "빈 문자는 안됩니다.")
    private String basicAddress;
    @Length(max = 50)
    @NotBlank(message = "빈 문자는 안됩니다.")
    private String detailedAddress;
    @Length(max = 50)
    @NotBlank(message = "빈 문자는 안됩니다.")
    private String zipcode;

    public MemberUpdateDto toServiceDto(){
        return MemberUpdateDto.builder()
                .name(name)
                .birth(birth)
                .email(email)
                .representativeName(representativeName)
                .phoneNumber(phoneNumber)
                .companyRegistrationNumber(companyRegistrationNumber)
                .corporateRegistrationNumber(corporateRegistrationNumber)
                .address(new Address(basicAddress,detailedAddress,zipcode))
                .build();
    }

}
