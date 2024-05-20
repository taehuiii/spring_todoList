package com.teamsparta.todoList.domain.exception

data class NameNotFoundException(val modelName: String, val name: String?) :
    RuntimeException("Model $modelName not found with given name: $name")