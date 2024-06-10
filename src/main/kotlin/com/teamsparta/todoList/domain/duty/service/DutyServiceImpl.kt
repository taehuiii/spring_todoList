package com.teamsparta.todoList.domain.duty.service

import com.teamsparta.todoList.domain.duty.dto.duty.*
import com.teamsparta.todoList.domain.duty.model.Duty
import com.teamsparta.todoList.domain.duty.model.toCmtResponse
import com.teamsparta.todoList.domain.duty.model.toResponse
import com.teamsparta.todoList.domain.duty.repository.CommentRepository
import com.teamsparta.todoList.domain.duty.repository.DutyRepository
import com.teamsparta.todoList.domain.exception.ModelNotFoundException
import com.teamsparta.todoList.domain.exception.NameNotFoundException
import com.teamsparta.todoList.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * getAllDUtyList
 * DutyList가
 * 1-1. 존재할때
 * 1-2. 존재하지 않을때
 *-> 전체 list를 요청하면?
 *
 * getDutyById
 * dutyId에 해당하는 duty가
 * 1-1. 존재할때
 * 1-2. 존재하지 않을 때
 *-> 특정 duty를 요청하면?
 *
 *
 * getDutyListByName
 * 작성자이름에 해당하는 duty가
 * 1-1. 존재할때
 * 1-2. 존재하지 않을 때
 *-> 작성자이름으로 조회를 요청하면?
 *
 *
 *addDuty
 * 할일 데이터가 있을 때
 * 사용자가 할일을 추가하면, 추가한다
 *
 * updateDuty
 * 1. 게시글 작성자가
 * 2. 게시글 작성자가 아닌 사람이
 * 할일을 수정하면 ,
 *
 * deleteDuty
 * 1. 게시글 작성자가
 * 2. 게시글 작성자가 아닌 사람이
 * 할일을 삭제하면
 *
 *
 * completeDuty
 * 1. 게시글 작성자가
 * 2. 게시글 작성자가 아닌 사람이
 * 할일 완료여부를 수정하면
 *
 *
 */


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
    override fun getAllDutyList(orderType: String): List<DutyCommentsResponseDto> {

        val duty = dutyRepository.findAll().map { it.toResponse() }.toMutableList()
        val commentList =
            duty.map { commentRepository.findAllByDutyId(it.id).map { it.toCmtResponse() }.toMutableList() }

        val orderedDutyList = toDutyListCommentResponseDtoResponse(duty, commentList)

        return if (orderType.uppercase() == "ASC") {
            orderedDutyList.sortedBy { it.date }
        } else {
            orderedDutyList.sortedByDescending { it.date }
        }

    }

    override fun getDutyById(dutyId: Long): DutyCommentsResponseDto {

        val duty = dutyRepository.findByIdOrNull(dutyId) ?: throw ModelNotFoundException("Duty", dutyId)
        val commentList = commentRepository.findAllByDutyId(dutyId).map { it.toCmtResponse() }.toMutableList()

        return toDutyCommentsResponseDtoResponse(commentList, duty.toResponse())

    }

    override fun getDutyListByName(filterName: String): List<DutyResponseDto> {

        val duty = dutyRepository.findByName(filterName) ?: throw NameNotFoundException("Duty", filterName)


        return duty.map { it.toResponse() }
    }


    @Transactional
    override fun addDuty(requestDto: AddDutyRequestDto): DutyResponseDto {

        val currentUser = SecurityContextHolder.getContext().authentication.principal as UserPrincipal

        return dutyRepository.save(
            Duty(
                title = requestDto.title,
                description = requestDto.description,
                date = requestDto.date,
                name = requestDto.name,
                userId = currentUser.id
            )
        ).toResponse()
    }

    @Transactional
    override fun updateDuty(dutyId: Long, requestDto: UpdateDutyRequestDto): DutyResponseDto {

        val duty = dutyRepository.findByIdOrNull(dutyId) ?: throw ModelNotFoundException("Duty", dutyId)
        val currentUser = SecurityContextHolder.getContext().authentication.principal as UserPrincipal

        if (duty.userId != currentUser.id) {
            throw IllegalAccessException("You are not allowed to update this Todo")
        }

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
        val currentUser = SecurityContextHolder.getContext().authentication.principal as UserPrincipal

        if (duty.userId != currentUser.id) {
            throw IllegalAccessException("You are not allowed to update this Todo")
        }



        dutyRepository.delete(duty)

    }

    @Transactional
    override fun completeDuty(dutyId: Long, requestDto: CompleteDutyRequestDto): DutyResponseDto {

        val duty = dutyRepository.findByIdOrNull(dutyId) ?: throw ModelNotFoundException("Duty", dutyId)
        val currentUser = SecurityContextHolder.getContext().authentication.principal as UserPrincipal

        if (duty.userId != currentUser.id) {
            throw IllegalAccessException("You are not allowed to update this Todo")
        }

        duty.complete = !duty.complete
        return duty.toResponse()

    }


}




