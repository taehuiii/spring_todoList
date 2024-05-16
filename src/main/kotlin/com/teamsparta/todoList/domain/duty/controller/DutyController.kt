package com.teamsparta.todoList.domain.duty.controller

import com.teamsparta.todoList.domain.duty.dto.AddDutyRequestDto
import com.teamsparta.todoList.domain.duty.dto.DutyResponseDto
import com.teamsparta.todoList.domain.duty.dto.UpdateDutyRequestDto
import com.teamsparta.todoList.domain.duty.service.DutyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/duties") //Handler Mapping에게 어떤 url을 담당하는지 알려줌 ( base url)
@RestController //Spring Bean으로 등록하도록 (REST : view가 아닌 data만을 응답하기 때문 )
class DutyController(  //Todo : 생성자 주입
    private val dutyService : DutyService //인터페이스 주입
) {

    //duty관련한 각각의 command에 대한 API 작성
    @GetMapping() //할일 목록 조회
    fun getDutyList(): ResponseEntity<List<DutyResponseDto>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body( dutyService.getAllDutyList() )

    }

    @GetMapping("/{dutyId}") //할일 단건 조회
    fun getDuty(@PathVariable dutyId : Long): ResponseEntity<DutyResponseDto>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body( dutyService.getDutyById(dutyId))
    }


    @PostMapping()  //duty 생성 -> RequestDTO를 Argument로 받아야함.
    fun AddDuty(@RequestBody addDutyRequest : AddDutyRequestDto): ResponseEntity<DutyResponseDto>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(dutyService.addDuty(addDutyRequest))
    }

    @PutMapping("/{dutyId}")  //duty 수정
    fun updateDuty(@PathVariable dutyId:Long, @RequestBody updateDutyRequest : UpdateDutyRequestDto): ResponseEntity<DutyResponseDto>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(dutyService.updateDuty(dutyId, updateDutyRequest))

    }

    @DeleteMapping("/{dutyId}") //duty 삭제
    fun deleteDuty( @PathVariable dutyId:Long ): ResponseEntity<Unit>{
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(dutyService.deleteDuty(dutyId))
    }

    /**Controller : service layer에서 발생한 예외에 대하여 status code와 함께 응답 던져줌.
     *
     * @ExceptionHandler( ModelNotFoundException ::class)
    fun handleModelNotFoundException(e: ModelNotFoundException): ResponseEntity<ErrorResponse> {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(e.message))
    }

     => 각 controller마다 exception 핸들링하면 중복코드 많이 발생
     => Spring : 전역적 예외처리 제공
     => @ControllerAdvice, @RestControllerAdvice을 예외처리class(생성 후)에 지정하고, @ExceptionHandler로 예외핸들링

     1. exception pkg -> dto pkg -> ErrorResponse dataclass 작성
     2. exception pkg에 GlobalExceptionHandler (class) 생성 -> @RestControllerAdvice
     3. @ExceptionHandler로 예외핸들링

            @ExceptionHandler(ModelNotFoundException::class)
            fun handleModelNotFoundException(e: ModelNotFoundException): ResponseEntity<ErrorResponse> {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(e.message))
            //Exception에서 발생한 message를 ErrorResponse로 감싸서 status code와 함께 응답

            }
     * */

}