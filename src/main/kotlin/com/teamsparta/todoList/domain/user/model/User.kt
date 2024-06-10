package com.teamsparta.todoList.domain.user.model

import com.teamsparta.todoList.domain.user.dto.UserResponseDto
import jakarta.persistence.*

@Entity
@Table( name="app_user")
class User(

    @Column(name="email", nullable=false)
    val email:String,

    @Column(name="password", nullable=false)
    val password : String,

    @Column(name="name", nullable=false)
    var name:String,

    @Column(name="role", nullable=false)
    var role:String = "member"


) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? =null
}

fun User.toResponse():UserResponseDto{
    return UserResponseDto(
        id = id!!,
        email = email,
        name=name
    )
}