package com.safeking.shop.domain.order.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class CreateMerchantUidTest {

    private CountDownLatch countDownLatch = new CountDownLatch(3);

    static String merchantUidByThread1 = "";
    static String merchantUidByThread2 = "";

    @Test
    void createMerchantUidTest() throws InterruptedException {
        log.info("merchantUid={}", createMerchantUid());
        
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                merchantUidByThread1 = createMerchantUid();
                countDownLatch.countDown();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                merchantUidByThread2 = createMerchantUid();
                countDownLatch.countDown();
            }
        });

        thread1.start();
        thread2.start();
        countDownLatch.countDown();
        countDownLatch.await();

        Assertions.assertThat(merchantUidByThread1).isNotEqualTo(merchantUidByThread2);
        
    } 
    
    public String createMerchantUid() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        LocalDateTime localDateTime = LocalDateTime.now(); //2023-02-05T00:52:30.229273
        String time = localDateTime.toString()
                .substring(2, 17)
                .replaceAll("T", "")
                .replaceAll("-", "")
                .replaceAll(":", "")
                ;
        
        return time+"_"+uuid;
    }
}
