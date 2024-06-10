package com.teamsparta.todoList.domain.duty.comment.service

import com.teamsparta.todoList.domain.duty.comment.dto.AddCommentRequestDto
import com.teamsparta.todoList.domain.duty.comment.dto.CommentResponseDto
import com.teamsparta.todoList.domain.duty.comment.dto.DeleteCommentRequestDto
import com.teamsparta.todoList.domain.duty.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todoList.domain.duty.comment.model.Comment
import com.teamsparta.todoList.domain.duty.comment.model.toResponse
import com.teamsparta.todoList.domain.duty.comment.repository.CommentRepository
import com.teamsparta.todoList.domain.duty.repository.DutyRepository
import com.teamsparta.todoList.domain.exception.ModelNotFoundException
import com.teamsparta.todoList.domain.exception.NameNotFoundException
import com.teamsparta.todoList.domain.exception.PwNotFoundException
import com.teamsparta.todoList.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val dutyRepository: DutyRepository,
) : CommentService {

    /**comment service*/
    @Transactional
    override fun addComment(dutyId: Long, requestDto: AddCommentRequestDto): CommentResponseDto {

        //1.duty 가져오고
        val duty = dutyRepository.findByIdOrNull(dutyId) ?: throw ModelNotFoundException("Duty", dutyId)

        //userId 가져오고
        val currentUser = SecurityContextHolder.getContext().authentication.principal as UserPrincipal

        //2. comment 정의
        val comment = Comment(
            content = requestDto.content, duty = duty, userId = currentUser.id
        )

        return commentRepository.save(comment).toResponse()


    }

    @Transactional
    override fun updateComment(dutyId: Long, commentId: Long, requestDto: UpdateCommentRequestDto): CommentResponseDto {

        //1. comment Entity 가져오고
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("Comment", commentId)



//        //2. 작성자이름, 비밀번호받아서 일치여부 확인하기
//        if (comment.name == requestDto.name) {
//            if (comment.pw == requestDto.pw) {
//                comment.content = requestDto.content //맞으면 수정
//            } else {
//                //비밀번호 틀리면 예외
//                throw PwNotFoundException("Comment", requestDto.pw)
//            }
//        } else {
//            //작성자 이름 틀리면 예외
//            throw NameNotFoundException("Comment", requestDto.name)
//
//        }

        //currentUser id 가져와서 비교하기
        val currentUser = SecurityContextHolder.getContext().authentication.principal as UserPrincipal

        if( comment.userId != currentUser.id){
            throw IllegalAccessException("You are not allowed to update this Comment ")
        }

        comment.content = requestDto.content
        //responseDTO로 반환
        return comment.toResponse()

    }

    @Transactional
    override fun deleteComment(dutyId: Long, commentId: Long, requestDto: DeleteCommentRequestDto) {
        //1. comment Entity 가져오고
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("Comment", commentId)
//
//        //2. 작성자이름, 비밀번호받아서 일치여부 확인하기
//        if (comment.name == requestDto.name) {
//            if (comment.pw == requestDto.pw) {
//
//            } else {
//                //비밀번호 틀리면 예외
//                throw PwNotFoundException("Comment", requestDto.pw)
//            }
//        } else {
//            //작성자 이름 틀리면 예외
//            throw NameNotFoundException("Comment", requestDto.name)
//
//        }

        val currentUser = SecurityContextHolder.getContext().authentication.principal as UserPrincipal


        if( comment.userId != currentUser.id){
            throw IllegalAccessException("You are not allowed to update this comment")
        }

        commentRepository.delete(comment)


    }

    override fun getCommentList(dutyId: Long): MutableList<CommentResponseDto> {

        return commentRepository.findAllByDutyId(dutyId).map { it.toResponse() }.toMutableList()
    }
}