package com.teamsparta.todoList.domain.duty.service

import com.teamsparta.todoList.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todoList.domain.comment.dto.CommentResponseDto
import com.teamsparta.todoList.domain.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todoList.domain.duty.dto.AddDutyRequestDto
import com.teamsparta.todoList.domain.duty.dto.CompleteDutyRequestDto
import com.teamsparta.todoList.domain.duty.dto.DutyResponseDto
import com.teamsparta.todoList.domain.duty.dto.UpdateDutyRequestDto

interface DutyService {


    /**Duty service*/
    fun getAllDutyList(): List<DutyResponseDto>
    fun getDutyById(dutyId: Long): DutyResponseDto

    fun addDuty(requestDto: AddDutyRequestDto): DutyResponseDto
    fun updateDuty(dutyId: Long, requestDto: UpdateDutyRequestDto): DutyResponseDto
    fun deleteDuty(dutyId: Long)

    fun completeDuty(dutyId: Long, requestDto: CompleteDutyRequestDto): DutyResponseDto

    /**comment service*/

    fun addComment(dutyId: Long, requestDto : AddCommentRequestDto) : CommentResponseDto
    fun updateComment(dutyId: Long, commentId:Long, requestDto : UpdateCommentRequestDto): CommentResponseDto
    fun deleteComment(dutyId: Long, commentId:Long)

}