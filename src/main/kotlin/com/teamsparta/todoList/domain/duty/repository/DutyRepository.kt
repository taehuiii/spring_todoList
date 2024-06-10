package com.teamsparta.todoList.domain.duty.repository

import com.teamsparta.todoList.domain.duty.model.Duty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DutyRepository : JpaRepository<Duty, Long> {
    fun findByName(name:String): List<Duty>?

}
