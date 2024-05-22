package com.teamsparta.todoList.domain.duty.dto

import com.teamsparta.todoList.domain.duty.comment.dto.CommentResponseDto

data class DutyListCommentResponseDto(
    val duties : MutableList<DutyResponseDto>,
    val comments: List<List<CommentResponseDto>?>?
)






