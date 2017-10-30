package br.com.zup.xyinc.exception

import groovy.json.JsonBuilder
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> response(Exception e, HttpStatus status, WebRequest request) {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)

        String body = new JsonBuilder([
                message: e.getMessage()
        ]).toString()

        return handleExceptionInternal(e, body, headers, status, request)
    }

    @ExceptionHandler(value = [ NotFoundException ])
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException e, WebRequest request) {
        return response(e, HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(value = [ AlreadyExistsException, ValidationException ])
    protected ResponseEntity<Object> handleAlreadyExistsException(Exception e, WebRequest request) {
        return response(e, HttpStatus.UNPROCESSABLE_ENTITY, request)
    }

    @ExceptionHandler(value = [ BadRequestException ])
    protected ResponseEntity<Object> handleBadRequestException(Exception e, WebRequest request) {
        return response(e, HttpStatus.BAD_REQUEST, request)
    }
}
