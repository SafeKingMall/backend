package com.safeking.shop.domain.user.domain.repository;

import com.safeking.shop.domain.user.domain.entity.NormalAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NormalAccountRepository extends JpaRepository<NormalAccount, Long> {

    @EntityGraph(attributePaths = {"member"})
    @Query("select na from NormalAccount na " +
            "where na.loginId = :loginId")
    Optional<NormalAccount> findByLoginIdFetchMember(String loginId);
}
