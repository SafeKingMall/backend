package com.safeking.shop.domain.order.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class UnixTimeStampToDateTest {

    @Test
    void test() {
      log.info("{}",convertUNIXTimeStamp2LocalDateTime(1675404786L));
      log.info("{}",convertUNIXTimeStamp2LocalDateTime(0L));
      log.info("{}", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
    }

    private LocalDateTime convertUNIXTimeStamp2LocalDateTime(Long unixTimeStamp) {

        if (unixTimeStamp == 0) return null;

        LocalDateTime localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTimeStamp),
                        TimeZone.getDefault().toZoneId());

        return localDateTime;
    }
}
