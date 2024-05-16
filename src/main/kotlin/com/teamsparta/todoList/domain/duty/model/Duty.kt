package com.teamsparta.todoList.domain.duty.model

import com.teamsparta.todoList.domain.duty.dto.DutyResponseDto
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name ="duty")
class Duty (

        @Column(name="title")
        var title : String,

        @Column(name="description")
        var description : String,

        @Column(name="date")
        val date : LocalDate,

        @Column(name="name")
        var name : String,

        //완료여부 ( enum)

        ){
        @Id
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        var id : Long? =null
}

fun Duty.toResponse(): DutyResponseDto {
        return DutyResponseDto(
                id = id!!,
                title = title,
                description = description,
                date =date,
                name = name
                //complete = complete,
        )
}