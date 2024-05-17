package com.teamsparta.todoList.domain.duty.service

import com.teamsparta.todoList.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todoList.domain.comment.dto.CommentResponseDto
import com.teamsparta.todoList.domain.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todoList.domain.comment.model.Comment
import com.teamsparta.todoList.domain.comment.model.toResponse
import com.teamsparta.todoList.domain.comment.repository.CommentRepository
import com.teamsparta.todoList.domain.duty.dto.AddDutyRequestDto
import com.teamsparta.todoList.domain.duty.dto.CompleteDutyRequestDto
import com.teamsparta.todoList.domain.duty.dto.DutyResponseDto
import com.teamsparta.todoList.domain.duty.dto.UpdateDutyRequestDto
import com.teamsparta.todoList.domain.duty.model.Duty
import com.teamsparta.todoList.domain.duty.model.toResponse
import com.teamsparta.todoList.domain.duty.repository.DutyRepository
import com.teamsparta.todoList.domain.exception.ModelNotFoundException
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
    private val commentRepository : CommentRepository


): DutyService  {

    /**duty service*/
    override fun getAllDutyList(): List<DutyResponseDto> {
        //DB에서 모든 duty(Entity)가져와서, dutyResponse(DTO)로 변환 후 반환
       return dutyRepository.findAll().map{ it.toResponse() }//map으로 각각의 duty Entity를 List<DutyResponse>로 ~
    }

    override fun getDutyById(dutyId: Long): DutyResponseDto {
        //만약 dutyId에 해당하는 duty 없다면 throw ModelNotFoundException
        //DB에서 id에 해당하는 duty Entity가져와서 dutyResponse(DTO)로 변환 후 반환
        val duty = dutyRepository.findByIdOrNull(dutyId) ?: throw ModelNotFoundException("Duty", dutyId)
        return duty.toResponse()
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
            )
        ).toResponse()
    }

    @Transactional
    override fun updateDuty(dutyId: Long, requestDto: UpdateDutyRequestDto): DutyResponseDto {
        //만약 dutyId에 해당하는 Duty가 없다면 throw ModelNotFoundException
        //DB에서 해당하는 dutyEntity가져와서 수정 및 저장 ->결과 DutyResponse(DTO)로 반환

        val duty = dutyRepository.findByIdOrNull(dutyId) ?: throw ModelNotFoundException("Duty", dutyId)
        val (title, description, name )= requestDto

        duty.title = title
        duty.description = description
        duty.name = name
        //dirty checking

        return dutyRepository.save(duty).toResponse()
    }

    @Transactional
    override fun deleteDuty(dutyId: Long) {
        //만약 dutyId에 해당하는 Duty가 없다면 throw ModelNotFoundException
        //DB에서 해당하는 Duty를 삭제, 연관된 Comment도 모두 삭제 -> todo : cacade

        val duty = dutyRepository.findByIdOrNull(dutyId) ?: throw ModelNotFoundException("Duty", dutyId)
        dutyRepository.delete(duty)

    }

    @Transactional
    override fun completeDuty(dutyId: Long, requestDto: CompleteDutyRequestDto) :DutyResponseDto {
        val duty = dutyRepository.findByIdOrNull(dutyId) ?: throw ModelNotFoundException("Duty", dutyId)
        duty.complete = !duty.complete
        return dutyRepository.save(duty).toResponse()
    }

    /**comment service*/
    @Transactional
    override fun addComment(dutyId: Long, requestDto: AddCommentRequestDto): CommentResponseDto {
        //만약 dutyId에 해당하는 duty Entity 없다면 throw ModelNotFoundException (선택한 할 일의 DB 저장 유무를 확인)
        //db에서 dutyId에 해당하는 duty Entity가져와서 comment Entity추가, db저장 -> responseDTO로 반환

        //Todo: 댓글 작성할 때 작성자 이름과 비밀번호 함께 받기 -> 일단 필드만 두면 되나?

        //1.duty 가져오고
        val duty = dutyRepository.findByIdOrNull(dutyId)?:throw ModelNotFoundException("Duty", dutyId)

        //2. comment 정의
        val comment = Comment(
            content = requestDto.content,
            name = requestDto.name,
            pw = requestDto.pw,
            duty = duty
        )

        return commentRepository.save(comment).toResponse()


    }

    @Transactional
    override fun updateComment(dutyId: Long, commentId: Long, requestDto: UpdateCommentRequestDto): CommentResponseDto {

        //db에서 commentId에 해당하는 commentEntidy가져와서 수정, db저장 -> responseDTO로 반환
        //만약 commentId에 해당하는 comment Entity 없다면 throw ModelNotFoundException

        //todo: 요기서 dutyId 받을 필요가 있나 ?
        //todo : 따로따로 부르는건 되는데(duty,comment) 그냥 comment repo에서 dutyid로 불러오는거만 안되는거 ?


        //Todo: 작성자이름, 비밀번호받아서 일치여부 확인하기
        //1. comment 가져오고
        val comment = commentRepository.findByIdOrNull(commentId)?:throw ModelNotFoundException("Comment", commentId)

        //2. 수정해주고
        comment.content = requestDto.content

        return commentRepository.save(comment).toResponse()

    }

    @Transactional
    override fun deleteComment(dutyId: Long, commentId: Long) {
        //db에서 commentId에 해당하는 commentEntidy가져와서 삭제
        //만약 commentId에 해당하는 comment Entity 없다면 throw ModelNotFoundException

        //Todo: 작성자이름, 비밀번호받아서 일치여부 확인하기
        //Todo: 성공 시 성공 상태코드 반환
        val comment = commentRepository.findByIdOrNull(commentId)?:throw ModelNotFoundException("Comment", commentId)

        commentRepository.delete(comment)
    }
}




