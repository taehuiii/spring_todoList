package com.teamsparta.todoList.domain.duty.service

import com.teamsparta.todoList.domain.duty.comment.dto.AddCommentRequestDto
import com.teamsparta.todoList.domain.duty.comment.dto.CommentResponseDto
import com.teamsparta.todoList.domain.duty.comment.dto.DeleteCommentRequestDto
import com.teamsparta.todoList.domain.duty.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todoList.domain.duty.comment.model.Comment
import com.teamsparta.todoList.domain.duty.comment.model.toResponse
import com.teamsparta.todoList.domain.duty.comment.repository.CommentRepository
import com.teamsparta.todoList.domain.duty.dto.*
import com.teamsparta.todoList.domain.duty.model.Duty
import com.teamsparta.todoList.domain.duty.model.toResponse
import com.teamsparta.todoList.domain.duty.repository.DutyRepository
import com.teamsparta.todoList.domain.exception.ModelNotFoundException
import com.teamsparta.todoList.domain.exception.NameNotFoundException
import com.teamsparta.todoList.domain.exception.PwNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/** service layer 요구사항
 * 1. 비즈니스로직 구현
 * 2. Transaction 경계 설정
 * 3. 예외에 대한 처리
 * */

@Service
class DutyServiceImpl(
    //생성자에 레포지토리 주입
    private val dutyRepository: DutyRepository,
    private val commentRepository: CommentRepository

) : DutyService {

    /**duty service*/
    override fun getAllDutyList(): MutableList<DutyCommentsResponseDto> {

        val duty = dutyRepository.findAll().map { it.toResponse() }.toMutableList()
        val commentList = duty.map { commentRepository.findAllByDutyId(it.id).map { it.toResponse() }.toMutableList() }

        return toDutyListCommentResponseDtoResponse(duty, commentList)

    }

    override fun getDutyById(dutyId: Long): DutyCommentsResponseDto {

        val duty = dutyRepository.findByIdOrNull(dutyId) ?: throw ModelNotFoundException("Duty", dutyId)
        val commentList = commentRepository.findAllByDutyId(dutyId).map { it.toResponse() }.toMutableList()

        return toDutyCommentsResponseDtoResponse(commentList, duty.toResponse())

    }

    override fun getDutyListByName(filterName: String): List<DutyResponseDto> {

        val duty = dutyRepository.findByName(filterName) ?: throw NameNotFoundException("Duty", filterName)

        return duty.map { it.toResponse() }
    }


    @Transactional
    override fun addDuty(requestDto: AddDutyRequestDto): DutyResponseDto {

        return dutyRepository.save(
            Duty(
                title = requestDto.title,
                description = requestDto.description,
                date = requestDto.date,
                name = requestDto.name,
            )
        ).toResponse()
    }

    @Transactional
    override fun updateDuty(dutyId: Long, requestDto: UpdateDutyRequestDto): DutyResponseDto {

        val duty = dutyRepository.findByIdOrNull(dutyId) ?: throw ModelNotFoundException("Duty", dutyId)
        val (title, description, name) = requestDto

        duty.title = title
        duty.description = description
        duty.name = name
        //dirty checking

        return duty.toResponse()
    }

    @Transactional
    override fun deleteDuty(dutyId: Long) {

        val duty = dutyRepository.findByIdOrNull(dutyId) ?: throw ModelNotFoundException("Duty", dutyId)
        dutyRepository.delete(duty)

    }

    @Transactional
    override fun completeDuty(dutyId: Long, requestDto: CompleteDutyRequestDto): DutyResponseDto {

        val duty = dutyRepository.findByIdOrNull(dutyId) ?: throw ModelNotFoundException("Duty", dutyId)
        duty.complete = !duty.complete
        return duty.toResponse()

    }


}




