package com.teamsparta.todoList.domain.duty.dto

import java.time.LocalDate

data class DutyResponseDto (
    val id : Long,
    val title : String,
    val description : String,
    val date : LocalDate,
    val name : String,
    //val complete : Boolean

    //Todo :  comment
    //Todo :  user


        )