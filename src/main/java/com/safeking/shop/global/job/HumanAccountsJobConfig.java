package com.safeking.shop.global.job;

import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class HumanAccountsJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;
    private final MemberService memberService;

    @Bean
    @Qualifier("humanAccountsJob")
    public Job humanAccountsJobJob(
            Step humanAccountsJobStep
            , Step conditionalFailStep
            , Step conditionalCompletedStep){
        return jobBuilderFactory
                .get("humanAccountsJob")
                .incrementer(new RunIdIncrementer())
                .start(humanAccountsJobStep)
                .on("FAILED").to(conditionalFailStep)//실패시
                .from(humanAccountsJobStep)
                .on("COMPLETED").to(conditionalCompletedStep)//성공시
                .from(humanAccountsJobStep)
                .end()
                .build();
        //배치가 실패시에 알림기능 log and email, 수동조작기능 추가


    }

    @JobScope
    @Bean
    public Step humanAccountsJobStep(Tasklet humanAccountsJobTasklet){
        return stepBuilderFactory
                .get("humanAccountsJobStep")
                .tasklet(humanAccountsJobTasklet)
                .build();
    }
    @JobScope
    @Bean
    public Step conditionalFailStep() {
        return stepBuilderFactory.get("conditionalFailStep")
                .tasklet((contribution, chunkContext) -> {
                    log.error("conditional Fail Step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @JobScope
    @Bean
    public Step conditionalCompletedStep() {
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
    public Tasklet humanAccountsJobTasklet() {
        return (contribution, chunkContext) -> {
            log.info("Run humanAccountsJobTasklet");
            memberService.humanAccountConverterBatch();

            return RepeatStatus.FINISHED;
        };
    }


}