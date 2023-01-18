//package com.safeking.shop.global.job;
//
//import com.safeking.shop.domain.coolsms.domain.entity.CoolSMS;
//import com.safeking.shop.domain.coolsms.domain.respository.SMSMemoryRepository;
//import com.safeking.shop.domain.coolsms.web.query.service.SMSService;
//import com.safeking.shop.domain.user.domain.entity.member.Member;
//import com.safeking.shop.domain.user.domain.repository.MemoryDormantRepository;
//import com.safeking.shop.domain.user.domain.repository.MemoryMemberRepository;
//import com.safeking.shop.global.job.JobConfig;
//import com.safeking.shop.global.job.MemoryClearJobConfig;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.batch.core.ExitStatus;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.test.JobLauncherTestUtils;
//import org.springframework.batch.test.context.SpringBatchTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBatchTest
//@ActiveProfiles("test")
//@SpringBootTest(classes = {
//        MemoryClearJobConfig.class
//        , MemoryMemberRepository.class
//        , SMSMemoryRepository.class
//        , MemoryDormantRepository.class
//        , SMSService.class
//        , SMSMemoryRepository.class
//})
//@Import(JobConfig.class)
//class MemoryClearJobConfigTest {
//    @Autowired
//    JobLauncherTestUtils jobLauncherTestUtils;
//    @Autowired
//    MemoryMemberRepository memoryMemberRepository;
//    @Autowired
//    SMSMemoryRepository smsMemoryRepository;
//    @Autowired
//    MemoryDormantRepository memoryDormantRepository;
//
//
//    @Test
//    public void success() throws Exception{
//        //when
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
//        //then
//        List<Member> dormantRepositoryAll = memoryDormantRepository.findAll();
//        List<CoolSMS> memoryRepositoryAll = smsMemoryRepository.findAll();
//        List<Member> memoryMemberRepositoryAll = memoryMemberRepository.findAll();
//
//        assertAll(
//                ()->assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED)
//                ,()->assertThat(dormantRepositoryAll.size()).isEqualTo(0)
//                ,()->assertThat(memoryRepositoryAll.size()).isEqualTo(0)
//                ,()->assertThat(memoryMemberRepositoryAll.size()).isEqualTo(0)
//        );
//    }
//}