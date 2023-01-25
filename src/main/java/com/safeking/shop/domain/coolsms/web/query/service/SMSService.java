package com.safeking.shop.domain.coolsms.web.query.service;

import com.safeking.shop.domain.coolsms.domain.entity.CoolSMS;
import com.safeking.shop.domain.coolsms.domain.respository.SMSMemoryRepository;
import com.safeking.shop.global.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class SMSService {
    private final SMSMemoryRepository coolSmsRepository;
    private static final String API_KEY = "NCSWYFY3FRFMGNRG";
    private static final String API_SECRET = "ZHMV0IR2M5L0V5E0K6PJKQ8FOUGPRWIN";

    private static final Message COOLSMS = new Message(API_KEY, API_SECRET);


    public String sendCodeToClient(String clientPhoneNumber) throws CoolsmsException {
        String code = createCode(clientPhoneNumber).getCode();

//        sendInformation(clientPhoneNumber, code, "code");
        return code;
    }

    public void sendErrorMessage(String clientPhoneNumber) throws CoolsmsException {

        sendInformation(clientPhoneNumber, "ERROR", "error");
    }

    public boolean checkCode(String clientCode, String clientPhoneNumber) {
        CoolSMS coolSMS = coolSmsRepository
                .findByClientPhoneNumber(clientPhoneNumber);

        //3분이 지나면 삭제
        if(coolSMS.isExpired()){
            coolSmsRepository.delete(coolSMS.getId());
            return false;
        }

        if (coolSMS.getCode().equals(clientCode)) {
            coolSmsRepository.delete(coolSMS.getId());
            return true;
        } else {
//            coolSmsRepository.delete(coolSMS.getId());
            return false;
        }
    }


    private CoolSMS createCode(String clientPhoneNumber) {
        Random rand = new Random();

        String code = "";
        for (int i = 0; i < 4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            code += ran;
        }
        CoolSMS coolSMS = new CoolSMS(code, clientPhoneNumber);
        coolSmsRepository.save(coolSMS);

        return coolSMS;
    }


    private static void sendInformation(String clientPhoneNumber, String information, String type) throws CoolsmsException {
        String text = type.equals("code") ? "SAFEKING의 인증번호는 " : "지금 배치 과정중의  ";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", clientPhoneNumber);
        params.put("from", "01082460887");
        params.put("type", "sms");
        params.put("text", text + "[" + information + "] 입니다.");
        COOLSMS.send(params);
    }


}
