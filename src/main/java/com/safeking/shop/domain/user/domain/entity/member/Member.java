package com.safeking.shop.domain.user.domain.entity.member;

import com.safeking.shop.domain.common.BaseTimeEntity;
import com.safeking.shop.domain.user.domain.entity.Address;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity @Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String birth;
    private String username;
    private String password;
    private String email;
    private String roles; //ROLE_USER

    private String phoneNumber;
    private String companyName;
    private String companyRegistrationNumber;
    private String corporateRegistrationNumber;
    private String representativeName;

    @Embedded
    private Address address;

    private String contact;

    private Boolean agreement;

    public List<String> getRoleList(){
        if(this.roles.length()>0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public void changeId(Long id) {
        this.id = id;
    }

    public void updateInfo(String name, String birth, String representativeName, String phoneNumber, String companyRegistrationNumber, String corporateRegistrationNumber, Address address){
        this.name = name;
        this.username = username;
        this.birth = birth;
        this.representativeName = representativeName;
        this.phoneNumber = phoneNumber;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.address = address;
    }

    public void updatePassword(String password){
        this.password=password;
    }
    public void addAuthenticationInfo(String name,String birth,String phoneNumber){

        this.name=name;
        this.birth=birth;
        this.phoneNumber=phoneNumber;

    }

    public void addMemberInfo(String companyName,String companyRegistrationNumber,String corporateRegistrationNumber,String representativeName,Address address,String contact){

        this.companyName=companyName;
        this.companyRegistrationNumber=companyRegistrationNumber;
        this.corporateRegistrationNumber=corporateRegistrationNumber;
        this.representativeName=representativeName;
        this.address=address;
        this.contact=contact;

    }

    public void addAgreement(Boolean agreement){
        this.agreement=agreement;
    }


    public void changePassword(String password){
        this.password=password;
    }

}
