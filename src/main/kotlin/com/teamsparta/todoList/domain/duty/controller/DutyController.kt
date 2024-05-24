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

@RequestMapping("/duties")
@RestController
class DutyController(
    private val dutyService: DutyService, //인터페이스 주입
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    //duty관련한 각각의 command에 대한 API 작성

    @GetMapping() //할일 목록 조회
    fun getDutyList(@RequestParam(defaultValue = "DESC") orderType: String): ResponseEntity<List<DutyCommentsResponseDto>> {

        var dutyList: MutableList<DutyCommentsResponseDto> = dutyService.getAllDutyList()
        var order = orderType.uppercase();

        if (order == "ASC") {
            dutyList.sortBy { it.date }
        } else {
            dutyList.sortByDescending { it.date }
        }


        return ResponseEntity
            .status(HttpStatus.OK)
            .body(dutyList)

    }

    @GetMapping("/name")
    fun getDutyByName(@RequestParam filterName: String): ResponseEntity<List<DutyResponseDto>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(dutyService.getDutyListByName(filterName))
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


}