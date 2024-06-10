package com.teamsparta.todoList.domain.duty.dto.duty

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class UpdateDutyRequestDto(

    @field:NotBlank(message = " 제목을 입력해주세요. ")
    @field:Size(max = 20, min = 1, message = "제목은 1자이상, 20자 이하입니다.")
    val title: String?,

    @field:NotBlank(message = " 본문을 입력해주세요. ")
    @field:Size(max = 1000, min = 1, message = "본문은 1자이상, 1000자 이하입니다.")
    val description: String?,


    val name: String,

    )
