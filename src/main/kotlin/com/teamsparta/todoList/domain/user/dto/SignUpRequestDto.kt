package com.teamsparta.todoList.domain.user.dto

data class SignUpRequestDto(

    val id: Long,
    val userId : String, //todo: ????
    val userPw: String,
    val userName: String,
    val nickname: String,

)
