package com.teamsparta.todoList.domain.duty.controller

import com.teamsparta.todoList.domain.duty.dto.comment.AddCommentRequestDto
import com.teamsparta.todoList.domain.duty.dto.comment.CommentResponseDto
import com.teamsparta.todoList.domain.duty.dto.comment.DeleteCommentRequestDto
import com.teamsparta.todoList.domain.duty.dto.comment.UpdateCommentRequestDto
import com.teamsparta.todoList.domain.duty.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RequestMapping("/duties/{dutyId}/comments")
@RestController
class CommentController(
    private val commentService: CommentService

) {

    @PostMapping()
    fun addComment(
        @PathVariable dutyId: Long,
        @RequestBody requestDto: AddCommentRequestDto
    ): ResponseEntity<CommentResponseDto> {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.addComment(dutyId, requestDto))
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable dutyId: Long,
        @PathVariable commentId: Long, @RequestBody requestDto: UpdateCommentRequestDto
    ): ResponseEntity<CommentResponseDto> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.updateComment(dutyId, commentId, requestDto))

    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable dutyId: Long,
        @PathVariable commentId: Long,
        @RequestBody requestDto: DeleteCommentRequestDto
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.deleteComment(dutyId, commentId, requestDto))

    }

    @GetMapping()
    fun getCommentList(@PathVariable dutyId: Long): ResponseEntity<List<CommentResponseDto>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.getCommentList(dutyId))
    }

}