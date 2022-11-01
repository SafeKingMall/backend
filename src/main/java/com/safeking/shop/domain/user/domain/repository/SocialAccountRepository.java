package com.safeking.shop.domain.user.domain.repository;

import com.safeking.shop.domain.user.domain.entity.OAuthProvider;
import com.safeking.shop.domain.user.domain.entity.SocialAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {

    @EntityGraph(attributePaths = {"member"})
    Optional<SocialAccount> findByOauthIdAndProviderFetchMember(Long oauthId, OAuthProvider provider);
}
