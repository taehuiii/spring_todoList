package com.teamsparta.todoList.domain.exception

import com.teamsparta.todoList.domain.exception.dto.ErrorResponse
import com.teamsparta.todoList.domain.user.exception.InvalidCredentialException
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(e: ModelNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(e.message))
    }

    @ExceptionHandler(NameNotFoundException::class)
    fun handleNameNotFoundException(e: NameNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(e.message))
    }

    @ExceptionHandler(PwNotFoundException::class)
    fun handlePwNotFoundException(e: PwNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(e.message))
    }

    @ExceptionHandler(InvalidCredentialException::class)
    fun handleInvalidCredentialException(e: InvalidCredentialException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse(e.message))
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(ex: MethodArgumentNotValidException): ErrorResponse {
        log.warn("잘못된 파라미터 입력", ex)
        val message = ex.bindingResult.fieldErrors.joinToString { "[${it.field}: ${it.defaultMessage}]" }
        return ErrorResponse("잘못된 값이 존재합니다. $message")
    }
}

/**Controller : service layer에서 발생한 예외에 대하여 status code와 함께 응답 던져줌.
 *
 * @ExceptionHandler( ModelNotFoundException ::class)
fun handleModelNotFoundException(e: ModelNotFoundException): ResponseEntity<ErrorResponse> {
return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(e.message))
}

=> 각 controller마다 exception 핸들링하면 중복코드 많이 발생
=> Spring : 전역적 예외처리 제공
=> @ControllerAdvice, @RestControllerAdvice을 예외처리class(생성 후)에 지정하고, @ExceptionHandler로 예외핸들링

1. exception pkg -> dto pkg -> ErrorResponse dataclass 작성
2. exception pkg에 GlobalExceptionHandler (class) 생성 -> @RestControllerAdvice
3. @ExceptionHandler로 예외핸들링

@ExceptionHandler(ModelNotFoundException::class)
fun handleModelNotFoundException(e: ModelNotFoundException): ResponseEntity<ErrorResponse> {

return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(e.message))
//Exception에서 발생한 message를 ErrorResponse로 감싸서 status code와 함께 응답

}
 * */