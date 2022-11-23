package com.safeking.shop.domain.user.web.response;


import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.Data;

@Data
public class MemberDetails {

    private String name;
    private String username;
    private String birth;

    private String representativeName;

    private String phoneNumber;

    private String companyRegistrationNumber;
    private String corporateRegistrationNumber;

    private String zipcode;
    private String basicAddress;
    private String detailedAddress;

    public MemberDetails(Member member) {
        this.name = member.getName();
        this.username = member.getUsername();
        this.birth = member.getBirth();
        this.representativeName = member.getRepresentativeName();
        this.phoneNumber = member.getPhoneNumber();
        this.companyRegistrationNumber = member.getCompanyRegistrationNumber();
        this.corporateRegistrationNumber = member.getCorporateRegistrationNumber();
        this.zipcode = member.getAddress().getZipcode();
        this.basicAddress = member.getAddress().getBasicAddress();
        this.detailedAddress = member.getAddress().getDetailedAddress();
    }
}
