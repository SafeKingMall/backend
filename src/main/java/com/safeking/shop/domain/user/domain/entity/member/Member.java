package com.safeking.shop.domain.user.domain.entity.member;

import com.safeking.shop.domain.admin.common.BaseTimeEntity;
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
    private String username;
    private String password;
    private String email;
    private String roles; //ROLE_USER

    private String phoneNumber;
    @Embedded
    private Address address;

    public List<String> getRoleList(){
        if(this.roles.length()>0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public void updateMemberInfo(String password,String email){

        this.password=password;
        this.email=email;

    }

}
