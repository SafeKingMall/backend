package com.safeking.shop.global.job;

import com.safeking.shop.domain.coolsms.web.query.service.SMSService;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WithdrawalJobConfig {
    /**
     * 30 일 후에 탈퇴
     **/
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final SMSService smsService;

    @Bean
    @Qualifier("withdrawalJob")
    public Job withdrawalJob(
            Step withdrawalJobStep
            , Step conditionalFailStepForWithdrawal
            , Step conditionalCompletedStepForWithdrawal){
        return jobBuilderFactory
                .get("withdrawalJob")
                .incrementer(new RunIdIncrementer())
                .start(withdrawalJobStep)
                .on("FAILED").to(conditionalFailStepForWithdrawal)//실패시
                .from(withdrawalJobStep)
                .on("COMPLETED").to(conditionalCompletedStepForWithdrawal)//성공시
                .from(withdrawalJobStep)
                .end()
                .build();
    }

    @JobScope
    @Bean
    public Step withdrawalJobStep(Tasklet withdrawalJobTasklet){
        return stepBuilderFactory
                .get("withdrawalJobStep")
                .tasklet(withdrawalJobTasklet)
                .build();
    }
    @JobScope
    @Bean
    public Step conditionalFailStepForWithdrawal() {
        return stepBuilderFactory.get("conditionalFailStepForWithdrawal")
                .tasklet((contribution, chunkContext) -> {
                    log.error("conditional Fail Step");
                    smsService.sendErrorMessage("01082460887");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @JobScope
    @Bean
    public Step conditionalCompletedStepForWithdrawal() {
        return stepBuilderFactory.get("conditionalCompletedStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("conditional Completed Step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @StepScope
    @Bean
    @Transactional
    public Tasklet withdrawalJobTasklet() {
        return (contribution, chunkContext) -> {
            memberRepository
                    .findByStatus(MemberStatus.WITHDRAWAL)
                    .stream()
                    .filter(Member::checkWithdrawalTime)
                    .forEach(member -> memberService.withdrawal(member.getUsername()));

            return RepeatStatus.FINISHED;
        };
    }
}