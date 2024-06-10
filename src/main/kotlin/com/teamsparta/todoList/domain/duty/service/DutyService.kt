package com.teamsparta.todoList.domain.duty.service

import com.teamsparta.todoList.domain.duty.dto.duty.*

interface DutyService {


    /**Duty service*/
    fun getAllDutyList(orderType: String): List<DutyCommentsResponseDto>
    fun getDutyById(dutyId: Long): DutyCommentsResponseDto

    fun getDutyListByName(filterName: String): List<DutyResponseDto>
    fun addDuty(requestDto: AddDutyRequestDto): DutyResponseDto
    fun updateDuty(dutyId: Long, requestDto: UpdateDutyRequestDto): DutyResponseDto
    fun deleteDuty(dutyId: Long)

    fun completeDuty(dutyId: Long, requestDto: CompleteDutyRequestDto): DutyResponseDto


}