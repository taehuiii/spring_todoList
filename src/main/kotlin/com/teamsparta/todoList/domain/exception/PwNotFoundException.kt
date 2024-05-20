package com.teamsparta.todoList.domain.exception

data class PwNotFoundException(val modelName: String, val pw: String?) :
    RuntimeException("Model $modelName not found with given pw: $pw")
