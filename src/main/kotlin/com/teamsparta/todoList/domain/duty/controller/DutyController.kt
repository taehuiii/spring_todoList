package com.teamsparta.todoList.domain.duty.controller

import com.teamsparta.todoList.domain.duty.dto.AddDutyRequestDto
import com.teamsparta.todoList.domain.duty.dto.DutyResponseDto
import com.teamsparta.todoList.domain.duty.dto.UpdateDutyRequestDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/duties") //Handler Mapping에게 어떤 url을 담당하는지 알려줌 ( base url)
@RestController //Spring Bean으로 등록하도록 (REST : view가 아닌 data만을 응답하기 때문 )
class DutyController(  //Todo : 생성자 주입
   //  private val dutyService : dutyService
) {
    //duty관련한 각각의 command에 대한 API 작성

    @GetMapping() //할일 목록 조회
    fun getDutyList(): ResponseEntity<List<DutyResponseDto>> {
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .body( dutyService.getAllDutyList() ) //Todo : dutyService.getAllDutyList() 구현
        TODO("not implemented")
    }

    @GetMapping("/{dutyId}") //할일 단건 조회
    fun getDuty(@PathVariable dutyId : Long): ResponseEntity<DutyResponseDto>{
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .body( dutyService.getDutyById(dutyId)) //Todo : dutyService.getDutyById(dutyId)
        TODO("not implemented")
    }


    @PostMapping()  //duty 생성 -> RequestDTO를 Argument로 받아야함.
    fun AddDuty(@RequestBody addDutyRequest : AddDutyRequestDto): ResponseEntity<DutyResponseDto>{
//        return ResponseEntity
//            .status(HttpStatus.CREATED)
//            .body(dutyService.addDutyRequest(addDutyRequest)) //Todo :dutyService.addDutyRequest(addDutyRequest)
        TODO("not implemented")
    }

    @PutMapping("/{dutyId}")  //duty 수정
    fun updateDuty(@PathVariable dutyId:Long, @RequestBody updateDutyRequest : UpdateDutyRequestDto): ResponseEntity<DutyResponseDto>{
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .body(dutyService.updateDuty(dutyId, updateDutyRequest)) //Todo : dutyService.updateDuty(dutyId, updateDutyRequest)
        TODO("not implemented")

    }

    @DeleteMapping("/{dutyId}") //duty 삭제
    fun deleteDuty( @PathVariable dutyId:Long ): ResponseEntity<Unit>{
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }



}