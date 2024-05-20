package com.teamsparta.todoList.domain.duty.controller

import com.teamsparta.todoList.domain.duty.dto.*
import com.teamsparta.todoList.domain.duty.service.DutyService
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RequestMapping("/duties") //Handler Mapping에게 어떤 url을 담당하는지 알려줌 ( base url)
@RestController //Spring Bean으로 등록하도록 (REST : view가 아닌 data만을 응답하기 때문 )
class DutyController(
    private val dutyService: DutyService, //인터페이스 주입

) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    //duty관련한 각각의 command에 대한 API 작성


    @GetMapping() //할일 목록 조회
    fun getDutyList(@RequestParam(defaultValue = "DESC") orderType: String): ResponseEntity<List<DutyResponseDto>> {

        var dutyList: MutableList<DutyResponseDto> = dutyService.getAllDutyList()
        orderType.uppercase();

        if (orderType == "ASC") {
            dutyList.sortBy { it.date }
        } else {
            dutyList.sortByDescending { it.date }
        }


        return ResponseEntity
            .status(HttpStatus.OK)
            .body(dutyList)

    }

    @GetMapping("/{dutyId}") //할일 단건 조회
    fun getDuty(@PathVariable dutyId: Long): ResponseEntity<DutyCommentsResponseDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(dutyService.getDutyById(dutyId))
    }


    @PostMapping()  //duty 생성 -> RequestDTO를 Argument로 받아야함.
    fun AddDuty(
        @Valid @RequestBody addDutyRequest: AddDutyRequestDto,
        bindingResult: BindingResult
    ): ResponseEntity<DutyResponseDto> {
        if (bindingResult.hasErrors()) {
            logger.error("errors : ${bindingResult.fieldErrors}")
            throw RuntimeException("errors ${bindingResult.fieldErrors}")//throw exception
        }
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(dutyService.addDuty(addDutyRequest))
    }

    @PutMapping("/{dutyId}")  //duty 수정
    fun updateDuty(
        @PathVariable dutyId: Long,
        @Valid @RequestBody updateDutyRequest: UpdateDutyRequestDto,
        bindingResult: BindingResult
    ): ResponseEntity<DutyResponseDto> {
        if (bindingResult.hasErrors()) {
            logger.error("errors : ${bindingResult.fieldErrors}")
            throw RuntimeException("errors ${bindingResult.fieldErrors}")//throw exception
        }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(dutyService.updateDuty(dutyId, updateDutyRequest))

    }

    @DeleteMapping("/{dutyId}") //duty 삭제
    fun deleteDuty(@PathVariable dutyId: Long): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(dutyService.deleteDuty(dutyId))
    }

    @PatchMapping("/{dutyId}")
    fun completeDuty(
        @PathVariable dutyId: Long,
        @RequestBody completeDutyRequest: CompleteDutyRequestDto
    ): ResponseEntity<DutyResponseDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(dutyService.completeDuty(dutyId, completeDutyRequest))
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