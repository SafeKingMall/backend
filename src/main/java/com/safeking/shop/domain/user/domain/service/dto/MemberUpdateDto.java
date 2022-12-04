package com.safeking.shop.domain.user.domain.service.dto;

import com.safeking.shop.domain.user.domain.entity.Address;
import lombok.Builder;
import lombok.Data;

@Data
public class MemberUpdateDto {
    private String name;
    private String birth;

    private String representativeName;

    private String phoneNumber;

    private String companyRegistrationNumber;
    private String corporateRegistrationNumber;
    private Address address;

    @Builder
    public MemberUpdateDto(
            String name
            , String birth
            , String representativeName
            , String phoneNumber
            , String companyRegistrationNumber
            , String corporateRegistrationNumber
            , Address address
    )
    {
        this.name = name;
        this.birth = birth;
        this.representativeName = representativeName;
        this.phoneNumber = phoneNumber;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.address = address;
    }
}
