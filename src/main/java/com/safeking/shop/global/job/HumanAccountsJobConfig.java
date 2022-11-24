package com.safeking.shop.global.job;

import com.safeking.shop.domain.user.domain.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Bean
    @Qualifier("humanAccountsJob")
    public Job humanAccountsJobJob(Step humanAccountsJobStep){
        return jobBuilderFactory
                .get("humanAccountsJob")
                .incrementer(new RunIdIncrementer())
                .start(humanAccountsJobStep)
                .build();
    }

    @JobScope//job 밑에서 실행이 되므로
    @Bean
    public Step humanAccountsJobStep(Tasklet humanAccountsJobTasklet){
        return stepBuilderFactory
                .get("humanAccountsJobStep")
                .tasklet(humanAccountsJobTasklet)
                .build();
    }
    @StepScope//Step 밑에서 실행이 되므로
    @Bean
    @Transactional
    public Tasklet humanAccountsJobTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("Run humanAccountsJobTasklet");

                memberRepository.findAll().stream()
                        .filter(member -> !member.getRoleList().stream().findFirst().get().equals("ROLE_ADMIN"))
                        .forEach(member -> member.convertHumanAccount());


                return RepeatStatus.FINISHED;
            }
        };
    }


}