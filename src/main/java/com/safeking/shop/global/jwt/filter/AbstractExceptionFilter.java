package com.safeking.shop.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.global.Error;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractExceptionFilter extends OncePerRequestFilter {

    protected final ObjectMapper objectMapper;

    protected AbstractExceptionFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected void generateErrorResponse(HttpServletResponse response,
                               int httpStatusCode,
                               Error error) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(httpStatusCode);
        response.getWriter().write(objectMapper.writer().writeValueAsString(error));
    }
}
