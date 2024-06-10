package com.teamsparta.todoList.domain.user.dto

data class SignUpRequestDto(

    val id: Long,
    val email : String,
    val password: String,
    val name: String,

)
