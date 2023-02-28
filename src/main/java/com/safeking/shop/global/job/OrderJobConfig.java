package com.safeking.shop.global.job;

import com.safeking.shop.domain.coolsms.web.query.service.SMSService;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.web.query.repository.OrderRepositoryCustom;
import com.safeking.shop.domain.order.web.query.repository.OrderRepositoryImpl;
import com.safeking.shop.domain.payment.domain.entity.PaymentStatus;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.payment.domain.repository.SafekingPaymentRepository;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.service.MemberService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
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
public class OrderJobConfig {
    /**
     * 아임포트 db 와 가맹점 db 를 동기화
     **/
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SMSService smsService;
    private final IamportClient client;
    private final SafekingPaymentRepository safekingPaymentRepository;

    @Bean
    @Qualifier("OrderJob")
    public Job OrderJob(
            Step OrderJobStep
            , Step conditionalFailStepForWithdrawal
            , Step conditionalCompletedStepForWithdrawal){
        return jobBuilderFactory
                .get("OrderJob")
                .incrementer(new RunIdIncrementer())
                .start(OrderJobStep)
                .on("FAILED").to(conditionalFailStepForWithdrawal)//실패시
                .from(OrderJobStep)
                .on("COMPLETED").to(conditionalCompletedStepForWithdrawal)//성공시
                .from(OrderJobStep)
                .end()
                .build();
    }

    @JobScope
    @Bean
    public Step OrderJobStep(Tasklet OrderJobTasklet){
        return stepBuilderFactory
                .get("OrderJobStep")
                .tasklet(OrderJobTasklet)
                .build();
    }
    @JobScope
    @Bean
    public Step conditionalFailStepForOrderJob() {
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
    public Step conditionalCompletedStepForOrderJob() {
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
    public Tasklet OrderJobTasklet() {
        return (contribution, chunkContext) -> {
            // orders Table 순회
            // orders 의 merchant_uid 로 pg 사 결제 상태를 조회
            // 둘의 상태가 다르다면 동기화
            List<SafekingPayment> result = safekingPaymentRepository.findAll();

            for (SafekingPayment safekingPayment : result) {
                Payment response = client
                        .paymentByImpUid(safekingPayment.getImpUid())
                        .getResponse();

                String iamPortStatus = response.getStatus();

                String dbStatus = safekingPayment.getStatus().getDescription();
                String DBStatus = changeStatus(dbStatus);

                if (iamPortStatus != DBStatus) {

                    safekingPayment.changeSafekingPayment(changeIamPortStatus(iamPortStatus) , response);
                }
            }

            return RepeatStatus.FINISHED;
        };
    }

    private String changeStatus(String dbStatus) {
        if (dbStatus.equals("READY")) {
            return "ready";
        } else if (dbStatus.equals("PAID")) {
            return "paid";
        }  else if (dbStatus.equals("CANCEL")) {
            return "cancelled";
        }  else {
            return "failed";
        }
    }

    private PaymentStatus changeIamPortStatus(String iamPortStatus) {
        if (iamPortStatus.equals("ready")) {
            return PaymentStatus.READY;
        } else if (iamPortStatus.equals("paid")) {
            return PaymentStatus.PAID;
        }  else if (iamPortStatus.equals("cancelled")) {
            return PaymentStatus.CANCEL;
        }  else {
            return PaymentStatus.FAILED;
        }
    }
}