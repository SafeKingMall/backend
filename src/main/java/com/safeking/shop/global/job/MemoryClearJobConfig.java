package com.safeking.shop.global.job;

import com.safeking.shop.domain.coolsms.domain.respository.SMSMemoryRepository;
import com.safeking.shop.domain.coolsms.web.query.service.SMSService;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryDormantRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryMemberRepository;
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
public class MemoryClearJobConfig {
    /**
     * custom Memory DB 의 얘기치 못한 데이터가 쌓이는 것을 방지
     **/

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;
    private final MemoryMemberRepository memoryMemberRepository;
    private final SMSMemoryRepository smsMemoryRepository;
    private final MemoryDormantRepository dormantRepository;
    private final SMSService smsService;

    @Bean
    @Qualifier("memoryClearJobJob")
    public Job memoryClearJobJob(
            Step memoryClearJobStep
            , Step conditionalFailStepMemory
            , Step conditionalCompletedStepMemory
    ){
        return jobBuilderFactory
                .get("memoryClearJobJob")
                .incrementer(new RunIdIncrementer())
                .start(memoryClearJobStep)
                .on("FAILED").to(conditionalFailStepMemory)//실패시
                .from(memoryClearJobStep)
                .on("COMPLETED").to(conditionalCompletedStepMemory)//성공시
                .from(memoryClearJobStep)
                .end()
                .build();
    }
    @JobScope
    @Bean
    public Step conditionalFailStepMemory() {
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
    public Step conditionalCompletedStepMemory() {
        return stepBuilderFactory.get("conditionalCompletedStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("conditional Completed Step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @JobScope//job 밑에서 실행이 되므로
    @Bean
    public Step memoryClearJobStep(Tasklet memoryClearJobTasklet){
        return stepBuilderFactory
                .get("memoryClearJobStep")
                .tasklet(memoryClearJobTasklet)
                .build();
    }
    @StepScope//Step 밑에서 실행이 되므로
    @Bean
    @Transactional
    public Tasklet memoryClearJobTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                memoryMemberRepository.clearStore();
                smsMemoryRepository.clearStore();
                dormantRepository.clearStore();

                log.info("memoryMemberRepository.findAll().size()= "+memoryMemberRepository.findAll().size());
                log.info("smsMemoryRepository.findAll().size()= "+smsMemoryRepository.findAll().size());
                log.info("dormantRepository.findAll().size()= "+dormantRepository.findAll().size());

                return RepeatStatus.FINISHED;
            }
        };
    }

}