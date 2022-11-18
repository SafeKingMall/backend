package com.safeking.shop.domain.user.domain.service.dto;

import com.safeking.shop.domain.user.domain.entity.Address;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class MemberInfoDto {

    private String companyName;
    private String companyRegistrationNumber;
    private String corporateRegistrationNumber;
    private String representativeName;
    private Address address;
    private String contact;

}
