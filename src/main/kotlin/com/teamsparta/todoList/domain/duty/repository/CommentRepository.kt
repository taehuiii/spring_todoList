package com.teamsparta.todoList.domain.duty.repository

import com.teamsparta.todoList.domain.duty.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByDutyId(dutyId: Long): MutableList<Comment>
}