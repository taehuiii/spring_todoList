package com.teamsparta.todoList.domain.duty.dto

import com.teamsparta.todoList.domain.duty.comment.dto.CommentResponseDto
import java.time.LocalDate


class DutyCommentsResponseDto(

    val id: Long,
    val title: String?,
    val description: String?,
    val date: LocalDate,
    val name: String,
    val complete: Boolean,
    val comments: MutableList<CommentResponseDto>?

)

fun toDutyCommentsResponseDtoResponse(
    commentResponseDto: MutableList<CommentResponseDto>,
    dutyResponseDto: DutyResponseDto
): DutyCommentsResponseDto {
    return DutyCommentsResponseDto(
        id = dutyResponseDto.id!!,
        title = dutyResponseDto.title,
        description = dutyResponseDto.description,
        date = dutyResponseDto.date,
        name = dutyResponseDto.name,
        complete = dutyResponseDto.complete,
        comments = commentResponseDto

    )
}

