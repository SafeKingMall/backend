package com.safeking.shop.domain.user.domain.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CheckSignUp {
    private Long id;
    private boolean check;
    private String username;

    private CheckSignUp(Long id, boolean check) {
        this.id = id;
        this.check = check;
    }

    private CheckSignUp(String username, boolean check) {
        this.check = check;
        this.username = username;
    }

    public static CheckSignUp createLoginUser(String username, boolean check) {
        return new CheckSignUp(username, check);
    }

    public static CheckSignUp createSignUpUser(Long id, boolean check) {
        return new CheckSignUp(id, check);
    }
}
