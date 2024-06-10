package com.teamsparta.todoList.infra.security

import com.teamsparta.todoList.infra.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler
){

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic { it.disable() } // BasicAuthenticationFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter 제외
            .formLogin { it.disable() } // UsernamePassworedAuthenticationFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter 제외
            .csrf { it.disable() } // CsrfFilter 제외
            .authorizeHttpRequests{ //추가
                it.requestMatchers(// 로그인, 회원가입, Swagger UI 및 API 문서 경로는 누구나 접근 가능
                    "/login",
                    "/signup",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                ).permitAll()
                    //.requestMatchers("/admin/**").hasRole("ADMIN")  // /admin/** 경로는 ADMIN 역할을 가진 사용자만 접근 가능
                    .anyRequest().authenticated() // 다른 모든 요청은 인증된 사용자만 접근 가능
            }

            // 기존 UsernamePasswordAuthenticationFilter 가 존재하던 자리에 JwtAuthenticationFilter 적용
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling{
                it.authenticationEntryPoint(authenticationEntryPoint)// 인증 실패 시 처리할 엔트리 포인트 설정
                it.accessDeniedHandler(accessDeniedHandler) // 추가!!
            }
            .build()
    }
}