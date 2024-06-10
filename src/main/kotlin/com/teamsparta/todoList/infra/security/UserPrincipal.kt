package com.teamsparta.todoList.infra.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal(
    val id: Long,
    val email: String,
    val authorities: Collection<GrantedAuthority> //
    // role대신 SpringSecurity에서 제공하는 GrantedAuthority을 권한으로써 사용
) {
    constructor(id: Long, email: String, roles: Set<String>) : this(
        id,
        email,
       roles.map { SimpleGrantedAuthority("ROLE_$it" ) }

)

}