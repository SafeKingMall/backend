package com.safeking.shop.domain.user.domain.entity.member;

import com.safeking.shop.domain.common.BaseMemberEntity;
import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity @Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public abstract class Member extends BaseMemberEntity {

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
    private Boolean accountNonLocked;
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    public List<String> getRoleList(){
        if(this.roles.length()>0){
            return Arrays.asList(this.roles.split(","));
        }

        return new ArrayList<>();
    }

    public void changeId(Long id) {
        this.id = id;
    }

    public void updateInfo(String name
                            ,String birth
                            ,String representativeName
                            ,String phoneNumber
                            ,String companyRegistrationNumber
                            ,String corporateRegistrationNumber
                            ,Address address
    ){
        this.name = name;
        this.birth = birth;
        this.representativeName = representativeName;
        this.phoneNumber = phoneNumber;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.address = address;
    }

    public void updateInfoFromDormant(String name
                                        ,String birth
                                        ,String password
                                        ,String email
                                        ,String companyName
                                        ,String representativeName
                                        ,String phoneNumber
                                        ,String companyRegistrationNumber
                                        ,String corporateRegistrationNumber
                                        ,Address address
                                        ,String contact
                                        ,Boolean agreement
                                        ,Boolean accountNonLocked
    ){
        this.name = name;
        this.birth = birth;
        this.password = password;
        this.email = email;
        this.companyName = companyName;
        this.representativeName = representativeName;
        this.phoneNumber = phoneNumber;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.address = address;
        this.contact = contact;
        this.agreement = agreement;
        this.accountNonLocked = accountNonLocked;
    }

    public void updatePassword(String password){
        this.password=password;
    }
    public void addAuthenticationInfo(String name,String birth,String phoneNumber){

        this.name=name;
        this.birth=birth;
        this.phoneNumber=phoneNumber;

    }

    public void addMemberInfo(
            String companyName
            ,String companyRegistrationNumber
            ,String corporateRegistrationNumber
            ,String representativeName
            ,Address address
            ,String contact)
    {
        this.companyName=companyName;
        this.companyRegistrationNumber=companyRegistrationNumber;
        this.corporateRegistrationNumber=corporateRegistrationNumber;
        this.representativeName=representativeName;
        this.address=address;
        this.contact=contact;

    }

    public void addCriticalItemsForDormant(String password, String email){
        this.password=password;
        this.email=email;
    }

    public boolean isCheckedItem(){
        return getPrerequisiteItem().stream().allMatch(item->item!=null);
    }

    private List<Object> getPrerequisiteItem() {
        List<Object> prerequisiteItem =new ArrayList<>();

        prerequisiteItem.add(this.name);
        prerequisiteItem.add(this.birth);
        prerequisiteItem.add(this.username);
        prerequisiteItem.add(this.password);
        prerequisiteItem.add(this.email);
        prerequisiteItem.add(this.roles);
        prerequisiteItem.add(this.phoneNumber);
        prerequisiteItem.add(this.companyName);
        prerequisiteItem.add(this.companyRegistrationNumber);
        prerequisiteItem.add(this.representativeName);
        prerequisiteItem.add(this.address);
        prerequisiteItem.add(this.agreement);

        return prerequisiteItem;
    }

    public void addAgreement(Boolean agreement){
        this.agreement=agreement;
    }


    public void changePassword(String password){
        this.password=password;
    }
    public void revertCommonAccounts(){
        this.accountNonLocked= !this.accountNonLocked;
        this.status=MemberStatus.COMMON;
    }

    public void convertHumanAccount(){
        Duration between = Duration.between(this.getLastLoginTime(), LocalDateTime.now());
        //지금은 임시로 10초로 설정
        if(between.getSeconds()>=10l){
            this.accountNonLocked=false;
            this.status=MemberStatus.HUMAN;

            //개인정보 지우기
            this.name = null;
            this.birth = null;
            this.email = null;
            this.phoneNumber = null;
            this.companyName = null;
            this.representativeName = null;
            this.companyRegistrationNumber = null;
            this.corporateRegistrationNumber = null;
            this.address = null;
            this.contact=null;
            this.agreement=null;
        }
    }
}