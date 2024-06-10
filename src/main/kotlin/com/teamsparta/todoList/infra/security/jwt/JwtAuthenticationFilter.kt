package com.teamsparta.todoList.infra.security.jwt

import com.teamsparta.todoList.infra.security.UserPrincipal
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.http.HttpHeaders

@Component
class JwtAuthenticationFilter(
    private val jwtPlugin: JwtPlugin
) : OncePerRequestFilter() {

    companion object {
        // Authorization Header로 부터 JWT를 획득하기 위한 정규식
        private val BEARER_PATTERN = Regex("^Bearer (.+?)$")
    }


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = request.getBearerToken() //1. 헤더에서 jwt 추출

        if (jwt != null) {
            jwtPlugin.validateToken(jwt) // 2. jwt 검증
                .onSuccess {
                    // 3. JWT로 부터 정보 획득
                    val userId = it.payload.subject.toLong()
                    val role = it.payload.get("role", String::class.java)
                    val email = it.payload.get("email", String::class.java)

                    //4. Authe객체에 담아서 "인증됨"표시하고 ->  SecurityContext에 저장

                    //4-1. Authe객체에 넣을 Principal 생성
                    val principal = UserPrincipal(
                        id = userId,
                        email = email,
                        roles = setOf(role) // collection이어서
                    )

                    //4-2. Authe객체 생성
                    val authentication = JwtAuthenticationToken(
                        principal = principal,
                        details =  WebAuthenticationDetailsSource().buildDetails(request)  // request로 부터 요청 상세정보 생성
                    )

                    // 4-3. SecurityContext에 authentication 객체 저장
                    SecurityContextHolder.getContext().authentication = authentication

                }
        }

        // 5. FilterChain 계속 진행
        filterChain.doFilter(request, response)
    }

    private fun HttpServletRequest.getBearerToken(): String? {
        //헤더 -> key AUTHORIZATION의 value 가져오기
        val headerValue = this.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        return BEARER_PATTERN.find(headerValue)?.groupValues?.get(1) //jwt 추출
    }
}