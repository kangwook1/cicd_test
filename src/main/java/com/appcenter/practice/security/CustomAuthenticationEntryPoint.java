package com.appcenter.practice.security;

import com.appcenter.practice.common.StatusCode;
import com.appcenter.practice.exception.CustomException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        CustomException ex=new CustomException(StatusCode.UNAUTHORIZED_MEMBER);

        //응답 상태코드에 status를 json으로 보냄.
        response.setStatus(ex.getStatusCode().getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //json으로 보낼 body에 해당하는 포맷을 설정함.
        // //response.getWriter().write(...)는 내부적으로 flush()가 발생해서 따로 호출할 필요 없음.
        response.getWriter().write(String.format("{\"message\": \"%s\"}", ex.getStatusCode().getMessage()));
    }
}
