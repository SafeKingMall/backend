package com.safeking.shop.domain.user.web.response;


import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDetails {

    private String name;
    private String username;
    private String birth;
    private String email;

    private String representativeName;

    private String phoneNumber;

    private String companyRegistrationNumber;
    private String corporateRegistrationNumber;

    private String basicAddress;
    private String detailedAddress;
    private String zipcode;

    public MemberDetails(Member member) {
        this.name = member.getName();
        this.username = member.getUsername();
        this.birth = member.getBirth();
        this.email = member.getEmail();
        this.representativeName = member.getRepresentativeName();
        this.phoneNumber = member.getPhoneNumber();
        this.companyRegistrationNumber = member.getCompanyRegistrationNumber();
        this.corporateRegistrationNumber = member.getCorporateRegistrationNumber();
        this.zipcode = member.getAddress().getZipcode();
        this.basicAddress = member.getAddress().getBasicAddress();
        this.detailedAddress = member.getAddress().getDetailedAddress();
    }
}
