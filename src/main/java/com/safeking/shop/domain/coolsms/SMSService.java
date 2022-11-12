package com.safeking.shop.domain.coolsms;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;
@Service
public class SMSService {

    public String sendCodeToClient(String clientPhoneNumber)throws CoolsmsException {
        String api_key = "NCSWYFY3FRFMGNRG";
        String api_secret = "ZHMV0IR2M5L0V5E0K6PJKQ8FOUGPRWIN";
        Message coolsms = new Message(api_key, api_secret);

        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", clientPhoneNumber);
        params.put("from", "01082460887");
        params.put("type", "sms");
        params.put("text", "김선주님! SAFEKING의 인증번호는 [" + numStr + "] 입니다.");

        coolsms.send(params); // 메시지 전송

        return numStr;
    }

}
