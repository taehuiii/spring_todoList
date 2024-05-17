package com.teamsparta.todoList.domain.comment.service

import com.teamsparta.todoList.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todoList.domain.comment.dto.CommentResponseDto
import com.teamsparta.todoList.domain.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todoList.domain.comment.model.Comment
import com.teamsparta.todoList.domain.comment.repository.CommentRepository
import com.teamsparta.todoList.domain.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

class CommentServiceImpl (
    private val commentRepository : CommentRepository
): CommentService {
}
