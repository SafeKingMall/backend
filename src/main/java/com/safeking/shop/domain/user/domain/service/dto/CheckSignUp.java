package com.safeking.shop.domain.user.domain.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckSignUp {
    private Long id;
    private boolean check;
    private String username;
    private boolean lock=false;

    private CheckSignUp(Long id, boolean check, boolean lock) {
        this.id = id;
        this.check = check;
        this.lock = lock;
    }

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

    public static CheckSignUp createDormant(Long id, boolean check, boolean lock) {
        return new CheckSignUp(id, check, lock);
    }
}
