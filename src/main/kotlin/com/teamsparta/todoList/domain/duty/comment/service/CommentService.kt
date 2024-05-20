package com.teamsparta.todoList.domain.duty.comment.service

import com.teamsparta.todoList.domain.duty.comment.dto.AddCommentRequestDto
import com.teamsparta.todoList.domain.duty.comment.dto.CommentResponseDto
import com.teamsparta.todoList.domain.duty.comment.dto.DeleteCommentRequestDto
import com.teamsparta.todoList.domain.duty.comment.dto.UpdateCommentRequestDto

interface CommentService {

    /**comment service*/

    fun addComment(dutyId: Long, requestDto: AddCommentRequestDto): CommentResponseDto
    fun updateComment(dutyId: Long, commentId: Long, requestDto: UpdateCommentRequestDto): CommentResponseDto
    fun deleteComment(dutyId: Long, commentId: Long, requestDto: DeleteCommentRequestDto)
    fun getCommentList(dutyId: Long) : MutableList<CommentResponseDto>
}