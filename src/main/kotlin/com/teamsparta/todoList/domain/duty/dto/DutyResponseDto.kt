package com.teamsparta.todoList.domain.duty.dto

import com.teamsparta.todoList.domain.comment.model.Comment
import java.time.LocalDate

data class DutyResponseDto (

    val id : Long,
    val title : String,
    val description : String,
    val date : LocalDate,
    val name : String,
    val complete : Boolean,
    //val comments : List<Comment>

    //Todo :  user


        )