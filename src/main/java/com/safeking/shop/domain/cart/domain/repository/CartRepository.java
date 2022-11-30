package com.safeking.shop.domain.cart.domain.repository;

import com.safeking.shop.domain.cart.domain.entity.Cart;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findByMember(Member member);

    //username 으로 cart 를 찾는 한방 쿼리


    @Query("select c from Cart c join fetch c.member m where m.username = :username")
    Optional<Cart> findCartByUsername(@Param("username") String username);



}
