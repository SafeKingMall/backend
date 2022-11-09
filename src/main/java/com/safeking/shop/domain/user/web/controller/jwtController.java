package com.safeking.shop.domain.user.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class jwtController {

    @GetMapping("/auth/success")
    @ResponseBody
    public String signInSuccess(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        log.info("/auth/success");
        String jwtToken = response.getHeader("Authorization");

        Map<String,String> map=new HashMap<String,String>();
        map.put("Authorization",jwtToken);
        redirectAttributes.addFlashAttribute("map",map);
        log.info(jwtToken);

        return "redirect:/auth/jwtToken";
    }

    @GetMapping("/auth/jwtToken")
    @ResponseBody
    public ResponseEntity<String> jwtToken(HttpServletRequest request){
        log.info("/auth/jwtToken");

        String jwtToken=null;

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if(flashMap!=null){
            Map<String, String> authorization = (Map<String, String>) flashMap.get("map");
            jwtToken = authorization.get("Authorization");
        }
        log.info("/auth/jwtToken"+jwtToken);

        return ResponseEntity.ok().header("Authorization",jwtToken).body("hello");
    }
}
