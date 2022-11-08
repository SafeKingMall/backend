package com.safeking.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PracticeController {

    @GetMapping("/")
    public String practice(){
        return "loginForm";
    }

}
