package com.teamsparta.todoList.domain.comment.repository

import com.teamsparta.todoList.domain.comment.dto.CommentResponseDto
import com.teamsparta.todoList.domain.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository :JpaRepository<Comment,Long>{
    fun findAllByDutyId(dutyId: Long): MutableList<Comment>
}