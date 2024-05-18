package com.teamsparta.todoList.domain.duty.repository
import com.teamsparta.todoList.domain.duty.dto.DutyResponseDto
import com.teamsparta.todoList.domain.duty.model.Duty
import org.springframework.data.jpa.repository.JpaRepository

interface DutyRepository :JpaRepository<Duty, Long >{
     fun findAllByDutyId(dutyId: Long): List<Duty>

}
