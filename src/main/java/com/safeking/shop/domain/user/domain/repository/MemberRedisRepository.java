package com.safeking.shop.domain.user.domain.repository;

import com.safeking.shop.domain.user.domain.entity.RedisMember;
import org.apache.log4j.spi.OptionHandler;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface MemberRedisRepository extends CrudRepository<RedisMember,String> {

    Optional<RedisMember>findByUsername(String username);

}
