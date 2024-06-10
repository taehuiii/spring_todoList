package com.teamsparta.todoList.infra.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamsparta.todoList.domain.exception.dto.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntrypoint: AuthenticationEntryPoint {

    override fun commence( //commence : 인증 과정이 실패하거나 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 호출됨
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED //응답상태 401로 설정
        response.contentType = MediaType.APPLICATION_JSON_VALUE //콘텐츠타입 json으로 설정
        response.characterEncoding = "UTF-8" //문자인코딩


        //HttpServletRespoonse에 Json형태의 데이터 설정해줘야함.
        //에러 시 -> ObjectMapper이용해서 errorResponse객체 -> json문자열로 직렬화 ( for 명확하고 표준화된 에러응답 )
        val objectMapper = ObjectMapper()
        val jsonString = objectMapper.writeValueAsString(ErrorResponse("JWT verification failed"))
        response.writer.write(jsonString) //응답writer에 작성
    }
}