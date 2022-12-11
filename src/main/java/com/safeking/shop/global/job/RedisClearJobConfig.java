package com.safeking.shop.global.job;

import com.safeking.shop.domain.coolsms.domain.respository.SMSMemoryRepository;
import com.safeking.shop.domain.coolsms.web.query.service.SMSService;
import com.safeking.shop.domain.user.domain.repository.MemoryDormantRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryMemberRepository;
import com.safeking.shop.domain.user.domain.service.RedisService;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisClearJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;
    private final RedisService redisService;
    private final SMSService smsService;
    @Bean
    @Qualifier("redisClearJobJob")
    public Job redisClearJobJob(
            Step redisClearJobStep
            , Step conditionalFailStepRedis
            , Step conditionalCompletedStepRedis
    ){
        return jobBuilderFactory
                .get("redisClearJobJob")
                .incrementer(new RunIdIncrementer())
                .start(redisClearJobStep)
                .start(redisClearJobStep)
                .on("FAILED").to(conditionalFailStepRedis)//실패시
                .from(redisClearJobStep)
                .on("COMPLETED").to(conditionalCompletedStepRedis)//성공시
                .from(redisClearJobStep)
                .end()
                .build();
    }
    @JobScope
    @Bean
    public Step conditionalFailStepRedis() {
        return stepBuilderFactory.get("conditionalFailStep")
                .tasklet((contribution, chunkContext) -> {
                    log.error("conditional Fail Step");
                    smsService.sendErrorMessage("01082460887");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @JobScope
    @Bean
    public Step conditionalCompletedStepRedis() {
        return stepBuilderFactory.get("conditionalCompletedStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("conditional Completed Step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @JobScope//job 밑에서 실행이 되므로
    @Bean
    public Step redisClearJobStep(Tasklet memoryClearJobTasklet){
        return stepBuilderFactory
                .get("redisClearJobStep")
                .tasklet(memoryClearJobTasklet)
                .build();
    }
    @StepScope//Step 밑에서 실행이 되므로
    @Bean
    @Transactional
    public Tasklet redisClearJobTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("redisClearJobTasklet");

                redisService.deleteAll();
                return RepeatStatus.FINISHED;
            }
        };
    }

}