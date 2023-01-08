package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.user.domain.entity.RedisMember;
import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final MemberRedisRepository redisRepository;
    // application load 시점에 redis를 한번 비운다.
    public void deleteAll(){
        Iterable<RedisMember> all = redisRepository.findAll();

        for (RedisMember redisMember : all) {
            if(redisMember != null) {
                redisRepository.delete(redisMember);
            }
        }
    }
}
