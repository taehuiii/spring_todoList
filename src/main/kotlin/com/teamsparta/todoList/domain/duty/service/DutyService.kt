package com.teamsparta.todoList.domain.duty.service

import com.teamsparta.todoList.domain.duty.dto.AddDutyRequestDto
import com.teamsparta.todoList.domain.duty.dto.DutyResponseDto
import com.teamsparta.todoList.domain.duty.dto.UpdateDutyRequestDto

interface DutyService {
    /** " Duty" aggregation : Duty , Comment */

    fun getAllDutyList() : List<DutyResponseDto>
    fun getDutyById( dutyId : Long) : DutyResponseDto

    fun addDuty( requestDto : AddDutyRequestDto) :DutyResponseDto
    fun updateDuty( dutyId: Long, requestDto: UpdateDutyRequestDto) : DutyResponseDto
    fun deleteDuty( dutyId: Long )

}