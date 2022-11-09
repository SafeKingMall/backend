package com.safeking.shop.domain.user.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class jwtController {

    @GetMapping("/auth/success")
    public String signInSuccess(HttpServletResponse response, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("jwtToken", response.getHeader("Authorization"));

        return "redirect:/auth/jwtToken";
    }

    @GetMapping("/auth/jwtToken")
    @ResponseBody
    public ResponseEntity<String> jwtToken(Model model){

        String jwtToken = (String) model.asMap().get("jwtToken");

        return ResponseEntity.ok()
                .body(jwtToken);
    }
}
