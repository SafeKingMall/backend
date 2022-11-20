package com.safeking.shop.domain.coolsms.web.query;

import com.safeking.shop.domain.coolsms.domain.entity.CoolSMS;
import com.safeking.shop.domain.coolsms.domain.respository.CoolSmsRepository;
import com.safeking.shop.domain.coolsms.domain.respository.SMSMemoryRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
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
        String code = createCode(clientPhoneNumber);

        sendInformation(clientPhoneNumber, code, "code");
        return code;
    }

    public void sendPasswordToClient(String clientPhoneNumber, String password) throws CoolsmsException {

        sendInformation(clientPhoneNumber, password, "password");
    }

    public boolean checkCode(String clientCode, String clientPhoneNumber) {
        CoolSMS coolSMS = coolSmsRepository
                .findByClientPhoneNumber(clientPhoneNumber)
                .orElseThrow(() -> new MemberNotFoundException("휴대번호가 일치하지 않습니다. 휴대전화 인증을 처음부터 다시 해주세요"));

        if (coolSMS.getCode().equals(clientCode)) {
            coolSmsRepository.delete(coolSMS.getId());
            return true;
        } else {
            coolSmsRepository.delete(coolSMS.getId());
            return false;
        }
    }


    private String createCode(String clientPhoneNumber) {
        Random rand = new Random();

        String code = "";
        for (int i = 0; i < 4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            code += ran;
        }
        CoolSMS coolSMS = new CoolSMS(code, clientPhoneNumber);
        coolSmsRepository.save(coolSMS);

        return coolSMS.getCode();
    }

    private static void sendInformation(String clientPhoneNumber, String information, String type) throws CoolsmsException {
        String text = type.equals("code") ? "SAFEKING의 인증번호는 " : "고객님의 임시 비밀번호는 ";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", clientPhoneNumber);
        params.put("from", "01082460887");
        params.put("type", "sms");
        params.put("text", text + "[" + information + "] 입니다.");
        COOLSMS.send(params);
    }


}
