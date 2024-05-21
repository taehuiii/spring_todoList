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
    override fun getAllDutyList(): MutableList<DutyResponseDto> {
        //DB에서 모든 duty(Entity)가져와서, dutyResponse(DTO)로 변환 후 반환
        return dutyRepository.findAll().map { it.toResponse() }
            .toMutableList()//map으로 각각의 duty Entity를 List<DutyResponse>로 ~
    }

    override fun getDutyById(dutyId: Long): DutyCommentsResponseDto {
        //만약 dutyId에 해당하는 duty 없다면 throw ModelNotFoundException
        //DB에서 id에 해당하는 duty Entity가져와서 dutyResponse(DTO)로 변환 후 반환
        val duty = dutyRepository.findByIdOrNull(dutyId) ?: throw ModelNotFoundException("Duty", dutyId)

        val commentList =commentRepository.findAllByDutyId(dutyId).map { it.toResponse() }.toMutableList()

        return toDutyCommentsResponseDtoResponse(commentList,duty.toResponse())

    }

    @Transactional
    override fun addDuty(requestDto: AddDutyRequestDto): DutyResponseDto {
        //requestDto를 Entity변환 후 DB에 저장 ->결과 CourseResponse(DTO)로 반환
        return dutyRepository.save(
            Duty(
                title = requestDto.title,
                description = requestDto.description,
                date = requestDto.date,
                name = requestDto.name,
                //comments = null
            )
        ).toResponse()
    }

    @Transactional
    override fun updateDuty(dutyId: Long, requestDto: UpdateDutyRequestDto): DutyResponseDto {
        //만약 dutyId에 해당하는 Duty가 없다면 throw ModelNotFoundException
        //DB에서 해당하는 dutyEntity가져와서 수정 및 저장 ->결과 DutyResponse(DTO)로 반환

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
        //만약 dutyId에 해당하는 Duty가 없다면 throw ModelNotFoundException
        //DB에서 해당하는 Duty를 삭제, 연관된 Comment도 모두 삭제 -> todo : cacade

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




