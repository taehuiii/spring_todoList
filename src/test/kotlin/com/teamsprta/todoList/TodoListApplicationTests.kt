package com.teamsprta.todoList

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan

@SpringBootTest
@ComponentScan(basePackages = ["com.teamsparta.todoList"])
class TodoListApplicationTests {

    @Test
    fun contextLoads() {
    }

}
