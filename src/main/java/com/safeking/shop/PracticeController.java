package com.safeking.shop;

import com.safeking.shop.global.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Slf4j
public class PracticeController {

    @GetMapping("/")
    public String practice(){
        //Oauth 확인 용 controller
        return "loginForm";
    }
    @GetMapping(value = "/error",produces =
            MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ErrorResult> error(){
        log.info("/error");
        ErrorResult errorResult = new ErrorResult("error", "error");
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST );
    }
    @GetMapping("/jwt")
    public String jwt(){
        return "jwt";
    }

}
