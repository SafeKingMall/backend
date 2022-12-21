package com.safeking.shop.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.global.annotation.ApiTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@ApiTest
public class MvcTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper om;

}
