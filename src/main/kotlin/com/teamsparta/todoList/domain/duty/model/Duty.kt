package com.teamsparta.todoList.domain.duty.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.teamsparta.todoList.domain.duty.comment.dto.CommentResponseDto
import com.teamsparta.todoList.domain.duty.comment.model.Comment
import com.teamsparta.todoList.domain.duty.dto.DutyResponseDto
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "duty")
class Duty(

    @Column(name = "title", nullable = false)
    var title: String?,

    @Column(name = "description", nullable = false)
    var description: String?,

    @Column(name = "date", nullable = false)
    val date: LocalDate,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "complete", nullable = false)
    var complete: Boolean = false,


    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

fun Duty.toResponse(): DutyResponseDto {
    return DutyResponseDto(
        id = id!!,
        title = title,
        description = description,
        date = date,
        name = name,
        complete = complete
    )
}

