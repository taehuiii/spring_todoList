package com.teamsparta.todoList.domain.duty.dto.duty

import com.teamsparta.todoList.domain.duty.dto.comment.CommentResponseDto

data class DutyListCommentResponseDto(
    val duties: MutableList<DutyResponseDto>,
    val comments: List<List<CommentResponseDto>?>?
)






