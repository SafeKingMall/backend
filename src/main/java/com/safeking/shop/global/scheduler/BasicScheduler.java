package com.safeking.shop.global.scheduler;

import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
public class BasicScheduler {
    @Qualifier("humanAccountsJob")
    private final Job humanAccountsJob;
    @Qualifier("memoryClearJobJob")
    private final Job memoryClearJobJob;
    private final JobLauncher jobLauncher;


    @Scheduled(cron = "*/15 * * * * *")
    public void humanAccountJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        System.out.println("LocalDateTime.now().getDayOfMonth()) = " + LocalDateTime.now().getDayOfMonth());

        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter(String.valueOf(LocalDateTime.now())))
        );

        jobLauncher.run(humanAccountsJob,jobParameters);
    }
    @Scheduled(cron = "*/20 * * * * *")
    public void memoryClearJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {


        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter(String.valueOf(LocalDateTime.now())))
        );

        jobLauncher.run(memoryClearJobJob,jobParameters);
    }


}