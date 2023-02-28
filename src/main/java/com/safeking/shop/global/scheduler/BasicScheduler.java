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
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class BasicScheduler {
    /**
     * 1. @Qualifier: 모두 다른 JOB 을 Autowired
     **/
    @Qualifier("humanAccountsJob")
    private final Job humanAccountsJob;
    @Qualifier("memoryClearJobJob")
    private final Job memoryClearJobJob;
    @Qualifier("redisClearJobJob")
    private final Job redisClearJobJob;
    @Qualifier("withdrawalJob")
    private final Job withdrawalJob;
    @Qualifier("OrderJob")
    private final Job OrderJob;

    private final JobLauncher jobLauncher;


    @Scheduled(cron = "0 15 4 ? * *")
    public void humanAccountJobRun()
            throws JobInstanceAlreadyCompleteException
            , JobExecutionAlreadyRunningException
            , JobParametersInvalidException
            , JobRestartException
    {
        log.info("휴면계정 BATCH");
        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
        );
        jobLauncher.run(humanAccountsJob,jobParameters);
        log.info("success humanAccountJobRun");
    }
    @Scheduled(cron = "0 20 4 ? * *")
    public void memoryClearJobRun()
            throws JobInstanceAlreadyCompleteException
            , JobExecutionAlreadyRunningException
            , JobParametersInvalidException
            , JobRestartException
    {
        log.info("memory BATCH");
        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
        );
        jobLauncher.run(memoryClearJobJob,jobParameters);
        log.info("success memoryClearJobRun");
    }
    @Scheduled(cron = "0 25 4 ? * *")
    public void redisClearJobRun()
            throws JobInstanceAlreadyCompleteException
            , JobExecutionAlreadyRunningException
            , JobParametersInvalidException
            , JobRestartException {
        log.info("redis BATCH");
        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
        );
        jobLauncher.run(redisClearJobJob,jobParameters);
        log.info("success redisClearJobRun");
    }

    @Scheduled(cron = "0 30 4 ? * *")
    public void withdrawalJobRun()
            throws JobInstanceAlreadyCompleteException
            , JobExecutionAlreadyRunningException
            , JobParametersInvalidException
            , JobRestartException {
        log.info("withdrawalJob BATCH");
        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
        );
        jobLauncher.run(withdrawalJob,jobParameters);
        log.info("success withdrawalJobRun");
    }

    @Scheduled(cron = "0 35 4 ? * *")
    public void OrderJobRun()
            throws JobInstanceAlreadyCompleteException
            , JobExecutionAlreadyRunningException
            , JobParametersInvalidException
            , JobRestartException {
        log.info("OrderJob BATCH");
        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
        );
        jobLauncher.run(OrderJob,jobParameters);
        log.info("success OrderJob");
    }


}