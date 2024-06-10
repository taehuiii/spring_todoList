package com.teamsparta.todoList.domain.user.repository

import com.teamsparta.todoList.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User,Long>{

    fun existsByEmail(email:String):Boolean
    fun findByEmail(email:String):User?
}