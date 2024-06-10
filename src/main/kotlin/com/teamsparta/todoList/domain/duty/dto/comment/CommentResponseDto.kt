package com.teamsparta.todoList.domain.duty.dto.comment

data class CommentResponseDto(
    val id: Long,
    val content: String,
    //val name: String,
    val userId :Long
)
