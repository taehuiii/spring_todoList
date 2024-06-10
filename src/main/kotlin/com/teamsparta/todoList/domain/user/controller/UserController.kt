package com.teamsparta.todoList.domain.user.controller

import com.teamsparta.todoList.domain.user.dto.LoginRequestDto
import com.teamsparta.todoList.domain.user.dto.LoginResponseDto
import com.teamsparta.todoList.domain.user.dto.SignUpRequestDto
import com.teamsparta.todoList.domain.user.dto.UserResponseDto
import com.teamsparta.todoList.domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/login")
    fun signIn(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.login(loginRequestDto))
    }
    @PostMapping("/signup")
    fun signUp(@RequestBody signUpRequestDto: SignUpRequestDto):ResponseEntity<UserResponseDto>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.signUp(signUpRequestDto))
    }

}
