package com.teamsparta.todoList.infra.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class JwtPlugin(
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.secret}") private val secret: String,
    @Value("\${auth.jwt.accessTokenExpirationHour}") private val accessTokenExpirationHour: Long,
) {

    /**try-catch 대신 Result ! */
    /**JWT를 검증하고 Result 타입으로 파싱된 클레임을 반환, 검증 실패 시 예외 반환*/
    fun validateToken(jwt: String): Result<Jws<Claims>> {
        return kotlin.runCatching { //runCatching : 주어진 블록을 실행하고 그 결과를 Result에 캡슐화
            val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8)) //HMAC SHA알고리즘 사용하여 SECRET에서 키 생성


            // jwt파서 초기화 -> 검증에 사용할 키 설정 -> 파서 객체 생성 -> 주어진 jwt문자열 파싱 및 서명 검증 -> 토큰의 클레임 추출해서 반환
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)
        }}

        fun generateAccessToken(subject: String, email: String,role:String): String {
            // subject, 만료기간과 role을 설정합니다.
            return generateToken(subject, email,role, Duration.ofHours(accessTokenExpirationHour))
        }


       fun generateToken(subject: String, email: String, role: String, expirationPeriod : Duration): String {
            // custom claim을 설정합니다.
            val claims: Claims = Jwts.claims()
                .add(mapOf("role" to role, "email" to email))
                .build()

            val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
            val now = Instant.now()

           //registered claim은 여기서 설정
            return Jwts.builder()
                .subject(subject)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationPeriod)))
                .claims(claims)
                .signWith(key)
                .compact()
        }

    }


