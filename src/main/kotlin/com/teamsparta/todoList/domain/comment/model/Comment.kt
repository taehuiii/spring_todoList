package com.teamsparta.todoList.domain.comment.model

import com.teamsparta.todoList.domain.comment.dto.CommentResponseDto
import com.teamsparta.todoList.domain.duty.dto.DutyResponseDto
import com.teamsparta.todoList.domain.duty.model.Duty
import jakarta.persistence.*

@Entity
@Table(name="comment")
class Comment(

  @Column(name="content", nullable = false)
  var content :String,

  @Column(name="name", nullable = false)
  var name :String,

  @Column(name="pw", nullable = false)
  var pw :String,

  @ManyToOne
  @JoinColumn(name="duty_id", nullable=false)
  var duty : Duty

){

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    var id:Long? =null
}

fun Comment.toResponse(): CommentResponseDto {
    return CommentResponseDto(
        id = id!!,
        content = content,
        name = name,
    )
}