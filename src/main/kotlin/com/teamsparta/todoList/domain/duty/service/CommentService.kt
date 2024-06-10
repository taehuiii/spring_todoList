package com.teamsparta.todoList.domain.duty.service

import com.teamsparta.todoList.domain.duty.dto.comment.AddCommentRequestDto
import com.teamsparta.todoList.domain.duty.dto.comment.CommentResponseDto
import com.teamsparta.todoList.domain.duty.dto.comment.DeleteCommentRequestDto
import com.teamsparta.todoList.domain.duty.dto.comment.UpdateCommentRequestDto

interface CommentService {

    /**comment service*/

    fun addComment(dutyId: Long, requestDto: AddCommentRequestDto): CommentResponseDto
    fun updateComment(dutyId: Long, commentId: Long, requestDto: UpdateCommentRequestDto): CommentResponseDto
    fun deleteComment(dutyId: Long, commentId: Long, requestDto: DeleteCommentRequestDto)
    fun getCommentList(dutyId: Long): MutableList<CommentResponseDto>
}