package olx.phonevalidator.web.advice

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.io.IOException

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [IllegalArgumentException::class])
    protected fun handleBadRequest(
        ex: RuntimeException, request: WebRequest
    ): ResponseEntity<Any> {
        val responseBody = ex.localizedMessage

        return handleExceptionInternal(
            ex, responseBody,
            HttpHeaders(), HttpStatus.BAD_REQUEST, request
        )
    }

    @ExceptionHandler(value = [NotFoundException::class])
    protected fun handleNotFound(
        ex: RuntimeException, request: WebRequest
    ): ResponseEntity<Any> {
        val responseBody = ex.localizedMessage

        return handleExceptionInternal(
            ex, responseBody,
            HttpHeaders(), HttpStatus.NOT_FOUND, request
        )
    }

    @ExceptionHandler(value = [IOException::class])
    protected fun handleIoException(
        ex: RuntimeException, request: WebRequest
    ): ResponseEntity<Any> {
        return handleExceptionInternal(
            ex, "",
            HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request
        )
    }
}