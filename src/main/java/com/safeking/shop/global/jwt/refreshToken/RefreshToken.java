package com.safeking.shop.global.jwt.refreshToken;

import com.safeking.shop.global.jwt.TokenUtils;
import com.safeking.shop.global.jwt.TokenUtils.TokenType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refreshtoken_id", nullable = false)
    private Long id;

    private String refreshToken;
    private String jwtToken;

    public RefreshToken(String refreshToken, String jwtToken) {
        this.refreshToken = refreshToken;
        this.jwtToken = jwtToken;
    }
}
