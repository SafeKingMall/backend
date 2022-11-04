package com.safeking.shop.domain.user.domain.repository;

import com.safeking.shop.domain.user.domain.entity.OAuthProvider;
import com.safeking.shop.domain.user.domain.entity.SocialAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {

    @EntityGraph(attributePaths = {"member"})
    @Query("select sa from SocialAccount sa " +
            "where sa.oauthId = :oauthId and sa.provider = :provider")
    Optional<SocialAccount> findByOauthIdAndProviderFetchMember(String oauthId, OAuthProvider provider);
}
