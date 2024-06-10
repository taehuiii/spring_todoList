package com.teamsparta.todoList.domain.user.service

import com.teamsparta.todoList.domain.exception.ModelNotFoundException
import com.teamsparta.todoList.domain.user.dto.LoginRequestDto
import com.teamsparta.todoList.domain.user.dto.LoginResponseDto
import com.teamsparta.todoList.domain.user.dto.SignUpRequestDto
import com.teamsparta.todoList.domain.user.dto.UserResponseDto
import com.teamsparta.todoList.domain.user.exception.InvalidCredentialException
import com.teamsparta.todoList.domain.user.model.User
import com.teamsparta.todoList.domain.user.model.toResponse
import com.teamsparta.todoList.domain.user.repository.UserRepository
import com.teamsparta.todoList.infra.security.jwt.JwtPlugin
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder, //인코더 주입
    private val jwtPlugin: JwtPlugin
) {

    fun signUp(request: SignUpRequestDto):UserResponseDto{
        if(userRepository.existsByEmail(request.email)){
            throw IllegalStateException("Email is already in use")
        }

        return userRepository.save(
            User(
            email = request.email,
            password = passwordEncoder.encode(request.password), //암호화
            name = request.name
        )
        ).toResponse()
    }

    fun login(request: LoginRequestDto): LoginResponseDto {

        //이메일 존재여부
        val user = userRepository.findByEmail(request.email)?: throw ModelNotFoundException("Email", null )

        //비밀번호 일치여부
        if( !passwordEncoder.matches(request.password, user.password)){
            throw InvalidCredentialException()
            }

        return LoginResponseDto(
            accessToken = jwtPlugin.generateAccessToken(
                subject = user.id.toString(),
                email = user.email,
                role = user.role
            )
        )
    }


}