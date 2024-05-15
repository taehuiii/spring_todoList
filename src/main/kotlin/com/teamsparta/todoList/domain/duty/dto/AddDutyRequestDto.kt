package com.teamsparta.todoList.domain.duty.dto

import java.time.LocalDate

data class AddDutyRequestDto (

    val title : String,
    val description : String,
    val date : LocalDate,
    val name : String,

        )
