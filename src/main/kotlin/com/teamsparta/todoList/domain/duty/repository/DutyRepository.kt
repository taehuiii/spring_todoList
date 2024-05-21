package com.teamsparta.todoList.domain.duty.repository

import com.teamsparta.todoList.domain.duty.dto.DutyResponseDto
import com.teamsparta.todoList.domain.duty.model.Duty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DutyRepository : JpaRepository<Duty, Long> {
    fun findAllById(id: Long): List<Duty>
    fun findByName(name:String): List<Duty>?

}
