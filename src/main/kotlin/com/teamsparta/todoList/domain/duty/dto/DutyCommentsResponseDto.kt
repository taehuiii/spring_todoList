package com.teamsparta.todoList.domain.duty.dto

import com.teamsparta.todoList.domain.duty.comment.dto.CommentResponseDto
import java.time.LocalDate


class DutyCommentsResponseDto(

    var id: Long,
    val title: String?,
    val description: String?,
    val date: LocalDate,
    val name: String,
    val complete: Boolean,
    val comments: MutableList<CommentResponseDto>?

)

fun toDutyCommentsResponseDtoResponse( //todo : param 순서 맞추기
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

fun toDutyListCommentResponseDtoResponse(
    dutiesRequestDto: MutableList<DutyResponseDto>,
    commentsRequestDto: List<MutableList<CommentResponseDto>?>
): MutableList<DutyCommentsResponseDto> {

    var dutyList: MutableList<DutyCommentsResponseDto> = mutableListOf()

    for (i: Int in 0 until dutiesRequestDto.size) {
        dutyList.add( DutyCommentsResponseDto(
            id = dutiesRequestDto[i].id!!,
            title = dutiesRequestDto[i].title,
            description = dutiesRequestDto[i].description,
            date = dutiesRequestDto[i].date,
            name = dutiesRequestDto[i].name,
            complete = dutiesRequestDto[i].complete,
            comments = commentsRequestDto[i]
        ))

    }
    return dutyList

}

