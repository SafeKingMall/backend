package com.safeking.shop.global.scheduler;

import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class BasicScheduler {
    @Qualifier("humanAccountsJob")
    private final Job humanAccountsJob;
    @Qualifier("memoryClearJobJob")
    private final Job memoryClearJobJob;
    @Qualifier("redisClearJobJob")
    private final Job redisClearJobJob;
    private final JobLauncher jobLauncher;


    @Scheduled(cron = "0 15 4 ? * *")
//    @Scheduled(cron = "*/10 * * * * *")
    public void humanAccountJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.info("휴면계정 BATCH");
        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter(String.valueOf(LocalDateTime.now().getDayOfMonth())))
//                Collections.singletonMap("requestTime", new JobParameter(String.valueOf(LocalDateTime.now().getMinute())))
        );

        jobLauncher.run(humanAccountsJob,jobParameters);
    }
    @Scheduled(cron = "0 20 4 ? * *")
    public void memoryClearJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.info("memory BATCH");
        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter(String.valueOf(LocalDateTime.now().getDayOfMonth())))
        );

        jobLauncher.run(memoryClearJobJob,jobParameters);
    }
    @Scheduled(cron = "0 25 4 ? * *")
    public void redisClearJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.info("redis BATCH");
        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter(String.valueOf(LocalDateTime.now().getDayOfMonth())))
        );
        jobLauncher.run(redisClearJobJob,jobParameters);
    }


}