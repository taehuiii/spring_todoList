package com.teamsparta.todoList.domain.duty.service

import com.teamsparta.todoList.domain.duty.comment.dto.AddCommentRequestDto
import com.teamsparta.todoList.domain.duty.comment.dto.CommentResponseDto
import com.teamsparta.todoList.domain.duty.comment.dto.DeleteCommentRequestDto
import com.teamsparta.todoList.domain.duty.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todoList.domain.duty.dto.*

interface DutyService {


    /**Duty service*/
    fun getAllDutyList(): MutableList<DutyResponseDto>
    fun getDutyById(dutyId: Long): DutyCommentsResponseDto

    fun getDutyListByName(filterName : String) : List<DutyResponseDto>
    fun addDuty(requestDto: AddDutyRequestDto): DutyResponseDto
    fun updateDuty(dutyId: Long, requestDto: UpdateDutyRequestDto): DutyResponseDto
    fun deleteDuty(dutyId: Long)

    fun completeDuty(dutyId: Long, requestDto: CompleteDutyRequestDto): DutyResponseDto



}