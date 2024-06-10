package com.teamsparta.todoList.domain.duty.controller

import com.teamsparta.todoList.domain.duty.dto.*
import com.teamsparta.todoList.domain.duty.service.DutyService
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
/**
 * "/duties"
 *
 * GetDutyList (GET)
 * 1. 로그인 한 회원이 조회요청을 보낼 때 ->  200 코드를 응답해야한다.
 * //없으면 ? null? empty ..?
 *
 * GetDutyByName (GET, "/name")
 *1. 로그인 한 회원이 조회요청을 보낼 때 -> 200코드를 응답해야한다.
 *
 * getDuty (GET, "/{dutyId}" )
 * 1. 로그인 한 사람이 조회요청을 보낼 떄 -> 200코드를 응답해야한다.
 *
 * addDuty (POST)
 * 1. 로그인 한 사람이 추가요청을 보낼 때 -> 201 created코드를 응답해야한다.
 * 2. bindingResult가 에러를 보내면 -> runtime exception을 응답해야 한다.
 *
 * * updateDuty (PUT, "/{dutyId}")
 * * 1. 해당글을 작성 한 사람이 수정요청을 보낼 때 -> 201 created코드를 응답해야한다.
 * 2. 글의 작성자가 아닐 경우, 403 코드를 응답해야한다.
 * 3. bindingResult가 에러를 보내면 -> runtime exception을 응답해야 한다.

 * deleteDuty (DELETE, "/{dutyId}")
 * * 1. 해당글을 작성 한 사람이 수정을 보낼 때 -> 201 created코드를 응답해야한다.
 * 2. 글의 작성자가 아닐 경우, 403 코드를 응답해야한다.
 * 3. bindingResult가 에러를 보내면 -> runtime exception을 응답해야 한다.
 *
 * completeDuty( Patch, "/{dutyId}")
 * 1. 해당글을 작성한 사람이 완료 요청을 보낼 때 -> 200코드를 응답해야한다.
 * 2. 작성자가 아닐경우, 403 코드를 응답해야한다.
 * */

@RequestMapping("/duties")
@RestController
class DutyController(
    private val dutyService: DutyService, //인터페이스 주입
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    //duty관련한 각각의 command에 대한 API 작성

    @GetMapping() //할일 목록 조회
    @PreAuthorize("hasRole('member')")
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
    @PreAuthorize("hasRole('member')")
    fun getDutyByName(@RequestParam filterName: String): ResponseEntity<List<DutyResponseDto>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(dutyService.getDutyListByName(filterName))
    }

    @GetMapping("/{dutyId}") //할일 단건 조회
    @PreAuthorize("hasRole('member')")
    fun getDuty(@PathVariable dutyId: Long): ResponseEntity<DutyCommentsResponseDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(dutyService.getDutyById(dutyId))
    }


    @PostMapping()  //duty 생성 -> RequestDTO를 Argument로 받아야함.
    @PreAuthorize("hasRole('member')")
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
    @PreAuthorize("hasRole('member')")
    fun updateDuty(
        @PathVariable dutyId: Long,
        @Valid @RequestBody updateDutyRequest: UpdateDutyRequestDto,
        bindingResult: BindingResult
    ): ResponseEntity<DutyResponseDto> {
        if (bindingResult.hasErrors()) {
            logger.error("errors : ${bindingResult.fieldErrors}")
            throw RuntimeException("errors ${bindingResult.fieldErrors}")//throw exception
        }

        return try {
            ResponseEntity
                .status(HttpStatus.OK)
                .body(dutyService.updateDuty(dutyId, updateDutyRequest))
        }catch(e: IllegalAccessException){
            ResponseEntity.status(403).build()
        }


    }

    @DeleteMapping("/{dutyId}") //duty 삭제
    @PreAuthorize("hasRole('member')")
    fun deleteDuty(@PathVariable dutyId: Long): ResponseEntity<Unit> {
        return try {
            ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(dutyService.deleteDuty(dutyId))
        }catch( e: IllegalAccessException){
            ResponseEntity
                .status(403).build()
        }
    }

    @PatchMapping("/{dutyId}")
    @PreAuthorize("hasRole('member')")
    fun completeDuty(
        @PathVariable dutyId: Long,
        @RequestBody completeDutyRequest: CompleteDutyRequestDto
    ): ResponseEntity<DutyResponseDto> {
        return try {
            ResponseEntity
                .status(HttpStatus.OK)
                .body(dutyService.completeDuty(dutyId, completeDutyRequest))
        } catch (e: IllegalAccessException) {
            ResponseEntity
                .status(403).build()
        }

    }}