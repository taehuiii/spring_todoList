package com.teamsparta.todoList.domain.duty.model

import com.teamsparta.todoList.domain.duty.dto.comment.CommentResponseDto
import jakarta.persistence.*

@Entity
@Table(name = "comment")
class Comment(

    @Column(name = "content", nullable = false)
    var content: String,

//    @Column(name = "name", nullable = false)
//    var name: String,
//
//    @Column(name = "pw", nullable = false)
//    var pw: String,

    @ManyToOne
    @JoinColumn(name = "duty_id", nullable = false)
    var duty: Duty,

    @Column(name="user_id", nullable=false)
    val userId:Long

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

fun Comment.toCmtResponse(): CommentResponseDto {
    return CommentResponseDto(
        id = id!!,
        content = content,
     //   name = name,
    userId = userId,


    )
}