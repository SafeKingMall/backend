package com.safeking.shop.domain.coolsms;

import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;
@Service
@RequiredArgsConstructor
public class SMSService {

    private final MemberRepository memberRepository;
    private static String CODE;
    private static String api_key = "NCSWYFY3FRFMGNRG";
    private static String api_secret = "ZHMV0IR2M5L0V5E0K6PJKQ8FOUGPRWIN";

    private static Message coolsms = new Message(api_key, api_secret);


    public String sendCodeToClient(String clientPhoneNumber)throws CoolsmsException {
        createCode();

        sendInformation(clientPhoneNumber,CODE,"code");
        return CODE;
    }

    public void sendPasswordToClient(String clientPhoneNumber,String password)throws CoolsmsException {

        sendInformation(clientPhoneNumber,password,"password");
    }

    public boolean checkCode(String clientCode){
        return CODE.equals(clientCode);
    }


    private static void createCode() {
        Random rand  = new Random();
        String code = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            code+=ran;
        }
        CODE=code;
    }



    private static void sendInformation(String clientPhoneNumber, String information, String type) throws CoolsmsException {
        String text = type.equals("code") ? "SAFEKING의 인증번호는 " : "고객님의 임시 비밀번호는 ";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", clientPhoneNumber);
        params.put("from", "01082460887");
        params.put("type", "sms");
        params.put("text",  text+"[" + information + "] 입니다.");
        coolsms.send(params);
    }


}
